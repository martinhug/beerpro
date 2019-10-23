/**
 * This app is split into three layers:
 * <p>
 * - data
 * - domain
 * - presentation
 * <p>
 * The data package/layer holds the repositories that connect to the backend services that hold the data. Note that these are minimal implementations to support the presentation.
 * <p>
 * The domain package holds the models that the data and presentation packages exchange.
 * <p>
 * The presentation package holds all the view related code. The activites and fragments in the presentation access
 * the repositories directly, which in turn return {@link androidx.lifecycle.LiveData} objects that the views can
 * observe. d
 * <p>
 * This architecture is a simplified version of the one shown in the <a href="https://android-developers.googleblog.com/2018/08/google-releases-source-for-google-io.html?m=1">Google I/O 2018 app</a>. To keep things simple, and
 * because doesn't contain that much business logic, the middle use-case layer was omitted.
 */
package ch.beerpro;