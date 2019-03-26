package request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;
import java.net.MalformedURLException;

public final class Request {

    public final String urlString;

    public final URL url;

    public final String error;

    public final Boolean isValid;

    public final Boolean isImage;

    public Request(String urlString) {
        final Pattern urlPattern = Pattern.compile("(http:\\/\\/|https:\\/\\/)?[a-z0-9]+([.]{1}[a-z0-9]+)*([.]{1}[a-z0-9]+)*([/a-z0-9_.-]+)?");
        final Pattern imagePattern = Pattern.compile("(.png|.jpg)");

        final Matcher matcher = urlPattern.matcher(urlString);
        final Matcher imageMatcher = imagePattern.matcher(urlString);

        if(matcher.find()) {

            String urlMatch = matcher.group(0);

            this.urlString = urlMatch;

            URL userURL = null;
            String error = null;
            boolean isValid = false;

            try {
                userURL = new URL(this.urlString);
                isValid = true;
            } catch(MalformedURLException exception) {
                exception.printStackTrace();
                error = "MalformedURLException for " + this.urlString;
                isValid = false;
            }

            this.error = error;
            this.url = userURL;
            this.isValid = isValid;

        } else {
            this.error = "User url does not follow url pattern (www.somewebsite.com) for: " + urlString;
            this.url = null;
            this.isValid = false;
            this.urlString = null;
        }

        this.isImage = imageMatcher.find();
    }
}