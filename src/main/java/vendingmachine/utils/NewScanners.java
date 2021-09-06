package vendingmachine.utils;

import java.lang.reflect.Field;
import java.util.Scanner;

public class NewScanners {

    private static Scanner scanner = getScanner();

    private NewScanners() {
    }

    public static String nextLine() {
        makeNewScannerIfScannerIsClosed();
        return scanner.nextLine();
    }

    public static int nextInt() {
        makeNewScannerIfScannerIsClosed();
        return scanner.nextInt();
    }

    public static void close() {
        scanner.close();
    }

    public static String next() {
        makeNewScannerIfScannerIsClosed();
        return scanner.next();
    }

    private static void makeNewScannerIfScannerIsClosed() {
        if (scannerIsClosed_ver1()) {
            scanner = getScanner();
        }
    }

    /**
     * 리플렉션 사용하여 closed 필드 검사
     * 단점: NoSuchFieldException, IllegalAccessException 발생시 후처리하기 애매
     */
    private static boolean scannerIsClosed_ver1() {
        try {
            return isClosed();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("리플렉션 중 에러 발생");
        }
        return true;
    }

    private static boolean isClosed() throws NoSuchFieldException, IllegalAccessException {
        Field closedField = Scanner.class.getDeclaredField("closed");
        closedField.setAccessible(true);
        return closedField.getBoolean(scanner);
    }

    /**
     * hasNext() 사용하여 closed 필드 검사
     * 단점: IllegalStateException이 다른 원인으로 발생할 가능성있음
     *      "Scanner closed" 라는 메세지로 검증하는 방법은 naive함
     */
    private static boolean scannerIsClosed_ver2() {
        try {
            scanner.hasNext();
        } catch (IllegalStateException e) {
            return "Scanner closed".equals(e.getMessage());
        }
        return false;
    }

    private static Scanner getScanner() {
        return new Scanner(System.in);
    }
}
