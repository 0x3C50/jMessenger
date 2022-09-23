import me.x150.Util;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilTest {
    @Test
    void testSignatures() throws Throwable {
        Method getMySignature = UtilTest.class.getDeclaredMethod("getMySignature", String.class, int.class, float.class, boolean.class);
        String s = Util.signatureOf(getMySignature);
        assertEquals("(Ljava/lang/String;IFZ)Ljava/lang/Number;", s);
    }

    @Test
    void testMake() {
        StringBuilder sb = Util.make(new StringBuilder(), stringBuilder -> stringBuilder.append(1).append(" ").append("abc"));
        assertEquals("1 abc", sb.toString());
    }

    @SuppressWarnings("unused")
    Number getMySignature(String string, int integer, float floating, boolean trueOrFalse) {
        return null;
    }
}
