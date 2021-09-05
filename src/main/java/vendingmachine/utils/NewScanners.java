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
        if (scannerIsClosed()) {
            // scannerIsClosed()가 true인 것이 테스트가 끝난 시점이란 보장이 없음.
            // 스캐너의 source가 끝나지 않았다면(인풋스트림에 원소가 남아있다면) scannerIsClosed()가 false를 반환
            // scanner.close();
            scanner = getScanner();
        }
    }

    private static boolean scannerIsClosed() {
        try {
            // 따라서 새로운 스캐너를 생성해줘야 하는 타이밍은
            // 1. 테스트 시작할 때 넣어준 입력 소스가 모두 소모됨 (sourceClosed로 검사)
            // 2. 테스트 후 직접 close()를 해줌 (closed로 검사)
            return scannerFieldIsTrue("sourceClosed") || scannerFieldIsTrue("closed");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("리플렉션 중 에러 발생");
        }
        return true;
    }

    private static boolean scannerFieldIsTrue(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field closedField = Scanner.class.getDeclaredField(fieldName);
        closedField.setAccessible(true);
        return closedField.getBoolean(scanner);
    }

    private static Scanner getScanner() {
        return new Scanner(System.in);
    }
}
