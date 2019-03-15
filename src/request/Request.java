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
	
	public Request(String urlString) {
		final Pattern urlPattern = Pattern.compile("(http:\\/\\/|https:\\/\\/)?[a-z0-9]+([-.]{1}[a-z0-9]+)*.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?");
		final Matcher matcher = urlPattern.matcher(urlString);
		
		this.urlString = urlString;

		if(matcher.matches()) {

			if(!urlString.contains("http://") || !urlString.contains("https://")) {
				urlString = "https://" + urlString;
			}

			URL userURL = null;
			String error = null;
			boolean isValid = false;

			try {
				userURL = new URL(urlString);
				isValid = true;
			} catch(MalformedURLException exception) {
				error = "MalformedURLException for " + urlString;
				isValid = false;
			}

			this.error = error;
			this.url = userURL;
			this.isValid = isValid;

		} else {
			this.error = "User url does not follow url pattern (www.somewebsite.com) for: " + urlString;
			this.url = null;
			this.isValid = false;
		}
	}
}