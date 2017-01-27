package helpers

import play.api.Play
import play.api.libs.oauth.{ConsumerKey, RequestToken}

/**
  * Created by carlos on 27/01/17.
  */
class TwitterCredentials {

  import play.api.Play.current

  protected def credentials = for {
    apiKey        <- Play.configuration.getString("twitter.apiKey")
    apiSecret     <- Play.configuration.getString("twitter.apiSecret")
    token         <- Play.configuration.getString("twitter.accessToken")
    tokenSecret   <- Play.configuration.getString("twitter.accessTokenSecret")
  } yield (ConsumerKey(apiKey, apiSecret), RequestToken(token, tokenSecret))

}
