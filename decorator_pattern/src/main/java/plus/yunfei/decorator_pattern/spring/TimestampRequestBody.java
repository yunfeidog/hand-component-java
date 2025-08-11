package plus.yunfei.decorator_pattern.spring;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimestampRequestBody {
}
