package cn.ollyice.framework.multidex;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by admin on 2018/6/19.
 */

public class Multidex {
    public static void install(Context context, File file){
        ClassLoader parent = Multidex.class.getClassLoader();
        String dexDir = context.getDir("support_dex",Context.MODE_PRIVATE)
                .getAbsolutePath();
        String jniDir = context.getDir("support_lib",Context.MODE_PRIVATE)
                .getAbsolutePath();
        DexClassLoader dexClassLoader = new DexClassLoader(file.getAbsolutePath(),
                dexDir, jniDir, parent);

        try {
            Object baseDexElements = getDexElements(getPathList(getPathClassLoader()));
            Object newDexElements = getDexElements(getPathList(dexClassLoader));
            Object allDexElements = combineArray(baseDexElements, newDexElements);
            Object pathList = getPathList(getPathClassLoader());
            ReflectUtil.setField(pathList.getClass(), pathList, "dexElements", allDexElements);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Object getDexElements(Object pathList) throws Exception {
        return ReflectUtil.getField(pathList.getClass(), pathList, "dexElements");
    }

    private static Object getPathList(Object baseDexClassLoader) throws Exception {
        return ReflectUtil.getField(Class.forName("dalvik.system.BaseDexClassLoader"), baseDexClassLoader, "pathList");
    }

    private static PathClassLoader getPathClassLoader() {
        PathClassLoader pathClassLoader = (PathClassLoader) Multidex.class.getClassLoader();
        return pathClassLoader;
    }

    private static Object combineArray(Object firstArray, Object secondArray) {
        Class<?> localClass = firstArray.getClass().getComponentType();

        //modify to sure plugin jar class is first use
        int firstArrayLength = Array.getLength(secondArray);
        int allLength = firstArrayLength + Array.getLength(firstArray);
        Object result = Array.newInstance(localClass, allLength);
        for (int k = 0; k < allLength; ++k) {
            if (k < firstArrayLength) {
                Array.set(result, k, Array.get(secondArray, k));
            } else {
                Array.set(result, k, Array.get(firstArray, k - firstArrayLength));
            }
        }
        return result;
    }
}
