package gov.va.api.health.argonaut.api.bundle;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.function.Function;

@Value
@Builder(toBuilder = true)
public class Bundler<F, T> {
    String linkConfig;
    Function<F, T> transformer;
    List<F> entries;
    Function<T, String> fullUrl;


    @AllArgsConstructor(staticName = "from", access = AccessLevel.PRIVATE)
    public static class BundlerType<FROM> {
        Class<FROM> from;

        public <TO> BundlerBuilder<FROM,TO> to(Class<TO> to) {
            return Bundler.builder();
        }
    }

    public static <FROM> BundlerType<FROM> from(Class<FROM> from) {
        return new BundlerType<>(from);
    }

    public Bundle<T> bundle() {

        return null;
    }


    public void deleteme() {
        Bundler.from(String.class).to(Integer.class).build();

    }
}
