package br.com.container.framework.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface Component {

    Scope scope() default Scope.SINGLETON;

    enum Scope {
        SINGLETON,
        PROTOTYPE;

        public boolean isSingleton() {
            return this.equals(Scope.SINGLETON);
        }
    }
}
