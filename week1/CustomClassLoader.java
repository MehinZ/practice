package geektime.hw;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class CustomClassLoader extends ClassLoader {

    private static final String SUFFIX = ".xlass";

    public static void main(String[] args) {
        final String className = "Hello";
        final String methodName = "hello";

        try {
            ClassLoader classLoader = new CustomClassLoader();
            Class<?> loadedClass = classLoader.loadClass(className);
            Object instance = loadedClass.getDeclaredConstructor().newInstance();
            Method method = loadedClass.getMethod(methodName);
            method.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String xlassName) throws ClassNotFoundException {
        byte[] decodeBytes = decodeXClass(xlassName);
        return defineClass(xlassName, decodeBytes, 0, decodeBytes.length);
    }

    private byte[] decodeXClass(String xlassName) throws ClassNotFoundException {
        try (InputStream ins = this.getClass().getClassLoader().getResourceAsStream(xlassName + SUFFIX)) {
            if (ins == null) {
                throw new ClassNotFoundException(xlassName);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = ins.read(b, 0, 1024)) > 0) {
                bos.write(b, 0, n);
            }
            byte[] encodedBytes = bos.toByteArray();
            return decode(encodedBytes);
        } catch (Exception e) {
            throw new ClassNotFoundException(xlassName);
        }
    }

    private byte[] decode(byte[] byteArray) {
        byte[] targetArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            targetArray[i] = (byte) (255 - byteArray[i]);
        }
        return targetArray;
    }
}
