package gov.uk.courtdata.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.Signature;

@Slf4j
@NoArgsConstructor
public class SignatureParser {
    public static String getFormattedMethodName(Signature signature) {
        String[] fullClassName = signature.getDeclaringTypeName().split("\\.");
        return fullClassName[fullClassName.length - 1] + "." + signature.getName();
    }
}
