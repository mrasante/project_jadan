package support.esri.com.fuse;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LogInActivityTest {

    @Rule
    public ActivityTestRule<LogInActivity> mActivityTestRule = new ActivityTestRule<>(LogInActivity.class);

    @Test
    public void logInActivityTest() {
        ViewInteraction twitterLoginButton = onView(
                allOf(withId(R.id.twitter_login_button), withText("Log in with Twitter"),
                        withParent(allOf(withId(R.id.social_layout),
                                withParent(withId(R.id.content_log_in)))),
                        isDisplayed()));
        twitterLoginButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Open"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton3.perform(click());
/*
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        withParent(withId(R.id.material_drawer_slider_layout)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(7, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        withParent(withId(R.id.material_drawer_slider_layout)),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(7, click()));

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        withParent(withId(R.id.material_drawer_slider_layout)),
                        isDisplayed()));
        recyclerView3.perform(actionOnItemAtPosition(7, click()));

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        withParent(withId(R.id.material_drawer_slider_layout)),
                        isDisplayed()));
        recyclerView4.perform(actionOnItemAtPosition(7, click()));

        ViewInteraction recyclerView5 = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        withParent(withId(R.id.material_drawer_slider_layout)),
                        isDisplayed()));
        recyclerView5.perform(actionOnItemAtPosition(7, click()));

        ViewInteraction recyclerView6 = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        withParent(withId(R.id.material_drawer_slider_layout)),
                        isDisplayed()));
        recyclerView6.perform(actionOnItemAtPosition(7, click()));

        ViewInteraction linearLayout = onView(
                allOf(withClassName(is("android.widget.LinearLayout")),
                        withParent(allOf(withId(R.id.material_drawer_recycler_view),
                                withParent(withId(R.id.material_drawer_slider_layout)))),
                        isDisplayed()));
        linearLayout.perform(longClick());

        ViewInteraction recyclerView7 = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        withParent(withId(R.id.material_drawer_slider_layout)),
                        isDisplayed()));
        recyclerView7.perform(actionOnItemAtPosition(7, click()));

        ViewInteraction linearLayout2 = onView(
                allOf(withClassName(is("android.widget.LinearLayout")),
                        withParent(allOf(withId(R.id.material_drawer_recycler_view),
                                withParent(withId(R.id.material_drawer_slider_layout)))),
                        isDisplayed()));
        linearLayout2.perform(longClick());

        ViewInteraction linearLayout3 = onView(
                allOf(withClassName(is("android.widget.LinearLayout")),
                        withParent(allOf(withId(R.id.material_drawer_recycler_view),
                                withParent(withId(R.id.material_drawer_slider_layout)))),
                        isDisplayed()));
        linearLayout3.perform(longClick());

        ViewInteraction recyclerView8 = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        withParent(withId(R.id.material_drawer_slider_layout)),
                        isDisplayed()));
        recyclerView8.perform(actionOnItemAtPosition(9, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withContentDescription("Open"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withContentDescription("Open"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction recyclerView9 = onView(
                allOf(withId(R.id.material_drawer_recycler_view),
                        withParent(withId(R.id.material_drawer_slider_layout)),
                        isDisplayed()));
        recyclerView9.perform(actionOnItemAtPosition(8, click()));
*/
    }

}
