package com.gem.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ImageLoader
{
	/**
	 * 鍥剧墖缂撳瓨鐨勬牳蹇冪被
	 */
	private LruCache<String, Bitmap> mLruCache;
	/**
	 * 绾跨▼姹�?
	 */
	private ExecutorService mThreadPool;
	/**
	 * 绾跨▼姹犵殑绾跨▼鏁伴噺锛岄粯璁や负1
	 */
	private int mThreadCount = 1;
	/**
	 * 闃熷垪鐨勮皟搴︽柟寮�?
	 */
	private Type mType = Type.LIFO;
	/**
	 * 浠诲姟闃熷垪
	 */
	private LinkedList<Runnable> mTasks;
	/**
	 * 杞鐨勭嚎绋�
	 */
	private Thread mPoolThread;
	private Handler mPoolThreadHander;

	/**
	 * 杩愯鍦║I绾跨▼鐨刪andler锛岀敤浜庣粰ImageView璁剧疆鍥剧墖
	 */
	private Handler mHandler;

	/**
	 * 寮曞叆涓�涓�间负1鐨勪俊鍙烽噺锛岄槻姝PoolThreadHander鏈垵濮嬪寲瀹屾�?
	 */
	private volatile Semaphore mSemaphore = new Semaphore(1);

	/**
	 * 寮曞叆涓�涓�间负1鐨勪俊鍙烽噺锛岀敱浜庣嚎绋嬫睜鍐呴儴涔熸湁涓�涓樆濉炵嚎绋嬶紝闃叉鍔犲叆浠诲姟鐨勯�熷害杩囧揩锛屼娇LIFO鏁堟灉涓嶆槑鏄�
	 */
	private volatile Semaphore mPoolSemaphore;

	private static ImageLoader mInstance;

	/**
	 * 闃熷垪鐨勮皟搴︽柟寮�?
	 * 
	 * @author zhy
	 * 
	 */
	public enum Type
	{
		FIFO, LIFO
	}


	/**
	 * 鍗曚緥鑾峰緱璇ュ疄渚嬪璞�
	 * 
	 * @return
	 */
	public static ImageLoader getInstance()
	{

		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new ImageLoader(1, Type.LIFO);
				}
			}
		}
		return mInstance;
	}

	private ImageLoader(int threadCount, Type type)
	{
		init(threadCount, type);
	}

	private void init(int threadCount, Type type)
	{
		// loop thread
		mPoolThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					// 璇锋眰涓�涓俊鍙烽�?
					mSemaphore.acquire();
				} catch (InterruptedException e)
				{
				}
				Looper.prepare();

				mPoolThreadHander = new Handler()
				{
					@Override
					public void handleMessage(Message msg)
					{
						mThreadPool.execute(getTask());
						try
						{
							mPoolSemaphore.acquire();
						} catch (InterruptedException e)
						{
						}
					}
				};
				// 閲婃斁涓�涓俊鍙烽�?
				mSemaphore.release();
				Looper.loop();
			}
		};
		mPoolThread.start();

		// 鑾峰彇搴旂敤绋嬪簭鏈�澶у彲鐢ㄥ唴瀛�
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheSize)
		{
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getRowBytes() * value.getHeight();
			};
		};

		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mPoolSemaphore = new Semaphore(threadCount);
		mTasks = new LinkedList<Runnable>();
		mType = type == null ? Type.LIFO : type;

	}

	/**
	 * 鍔犺浇鍥剧墖
	 * 
	 * @param path
	 * @param imageView
	 */
	public void loadImage(final String path, final ImageView imageView)
	{
		// set tag
		imageView.setTag(path);
		// UI绾跨�?
		if (mHandler == null)
		{
			mHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
					ImageView imageView = holder.imageView;
					Bitmap bm = holder.bitmap;
					String path = holder.path;
					if (imageView.getTag().toString().equals(path))
					{
						imageView.setImageBitmap(bm);
					}
				}
			};
		}

		Bitmap bm = getBitmapFromLruCache(path);
		if (bm != null)
		{
			ImgBeanHolder holder = new ImgBeanHolder();
			holder.bitmap = bm;
			holder.imageView = imageView;
			holder.path = path;
			Message message = Message.obtain();
			message.obj = holder;
			mHandler.sendMessage(message);
		} else
		{
			addTask(new Runnable()
			{
				@Override
				public void run()
				{

					ImageSize imageSize = getImageViewWidth(imageView);

					int reqWidth = imageSize.width;
					int reqHeight = imageSize.height;

					Bitmap bm = decodeSampledBitmapFromResource(path, reqWidth,
							reqHeight);
					addBitmapToLruCache(path, bm);
					ImgBeanHolder holder = new ImgBeanHolder();
					holder.bitmap = getBitmapFromLruCache(path);
					holder.imageView = imageView;
					holder.path = path;
					Message message = Message.obtain();
					message.obj = holder;
					// Log.e("TAG", "mHandler.sendMessage(message);");
					mHandler.sendMessage(message);
					mPoolSemaphore.release();
				}
			});
		}

	}
	
	/**
	 * 娣诲姞涓�涓换鍔�
	 * 
	 * @param runnable
	 */
	private synchronized void addTask(Runnable runnable)
	{
		try
		{
			// 璇锋眰淇″彿閲忥紝闃叉mPoolThreadHander涓簄ull
			if (mPoolThreadHander == null)
				mSemaphore.acquire();
		} catch (InterruptedException e)
		{
		}
		mTasks.add(runnable);
		mPoolThreadHander.sendEmptyMessage(0x110);
	}

	/**
	 * 鍙栧嚭涓�涓换鍔�
	 * 
	 * @return
	 */
	private synchronized Runnable getTask()
	{
		if (mType == Type.FIFO)
		{
			return mTasks.removeFirst();
		} else if (mType == Type.LIFO)
		{
			return mTasks.removeLast();
		}
		return null;
	}
	
	/**
	 * 鍗曚緥鑾峰緱璇ュ疄渚嬪璞�
	 * 
	 * @return
	 */
	public static ImageLoader getInstance(int threadCount, Type type)
	{

		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new ImageLoader(threadCount, type);
				}
			}
		}
		return mInstance;
	}


	/**
	 * 鏍规嵁ImageView鑾峰緱閫傚綋鐨勫帇缂╃殑瀹藉拰楂�?
	 * 
	 * @param imageView
	 * @return
	 */
	private ImageSize getImageViewWidth(ImageView imageView)
	{
		ImageSize imageSize = new ImageSize();
		final DisplayMetrics displayMetrics = imageView.getContext()
				.getResources().getDisplayMetrics();
		final LayoutParams params = imageView.getLayoutParams();

		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getWidth(); // Get actual image width
		if (width <= 0)
			width = params.width; // Get layout width parameter
		if (width <= 0)
			width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
																	// maxWidth
																	// parameter
		if (width <= 0)
			width = displayMetrics.widthPixels;
		int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getHeight(); // Get actual image height
		if (height <= 0)
			height = params.height; // Get layout height parameter
		if (height <= 0)
			height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
																		// maxHeight
																		// parameter
		if (height <= 0)
			height = displayMetrics.heightPixels;
		imageSize.width = width;
		imageSize.height = height;
		return imageSize;

	}

	/**
	 * 浠嶭ruCache涓幏鍙栦竴寮犲浘鐗囷紝濡傛灉涓嶅瓨鍦ㄥ氨杩斿洖null銆�
	 */
	private Bitmap getBitmapFromLruCache(String key)
	{
		return mLruCache.get(key);
	}

	/**
	 * 寰�LruCache涓坊鍔犱竴寮犲浘鐗�?
	 * 
	 * @param key
	 * @param bitmap
	 */
	private void addBitmapToLruCache(String key, Bitmap bitmap)
	{
		if (getBitmapFromLruCache(key) == null)
		{
			if (bitmap != null)
				mLruCache.put(key, bitmap);
		}
	}

	/**
	 * 璁＄畻inSampleSize锛岀敤浜庡帇缂╁浘鐗�?
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight)
	{
		// 婧愬浘鐗囩殑瀹藉�?
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth && height > reqHeight)
		{
			// 璁＄畻鍑哄疄闄呭搴�?拰鐩爣瀹藉害鐨勬瘮鐜�
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}

	/**
	 * 鏍规嵁璁＄畻鐨刬nSampleSize锛屽緱鍒板帇缂╁悗鍥剧墖
	 * 
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth, int reqHeight)
	{
		// 绗竴娆¤В鏋愬皢inJustDecodeBounds璁剧疆涓簍rue锛屾潵鑾峰彇鍥剧墖澶у�?
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		// 璋冪敤涓婇潰瀹氫箟鐨勬柟娉曡绠梚nSampleSize鍊�
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 浣跨敤鑾峰彇鍒扮殑inSampleSize鍊煎�?娆¤В鏋愬浘鐗�
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
		
		return bitmap;
	}

	private class ImgBeanHolder
	{
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}

	private class ImageSize
	{
		int width;
		int height;
	}

	/**
	 * 鍙嶅皠鑾峰緱ImageView璁剧疆鐨勬渶澶у搴﹀拰楂樺害
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName)
	{
		int value = 0;
		try
		{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
			{
				value = fieldValue;

				Log.e("TAG", value + "");
			}
		} catch (Exception e)
		{
		}
		return value;
	}

}
