package cn.com.incito.wisdom.sdk.cache.disk.naming;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeFileNameGenerator implements FileNameGenerator {
    private DateFormat format;

    public TimeFileNameGenerator(String format) {
        this.format = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss", Locale.getDefault());
    }

    @Override
    public String generate(String uri) {
        return format.format(uri);
    }
}
