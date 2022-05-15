package com.example.taskmaster;




import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

 @Rule
  public ActivityScenarioRule rule= new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.example.taskmaster", appContext.getPackageName());
    }

    @Test
    public void AllTasks() {
        // Type text and then press the button.
        onView(withId(R.id.MyTasks)).perform(click());

        onView(withId(R.id.textView)).check(matches(withText("All Tasks")));

    }




    @Test
    public void AddTask() {
        onView(withId(R.id.Addtask)).perform(click());
        // Type text and then press the button.
        onView(withId(R.id.title)).perform(typeText("T1"),
                closeSoftKeyboard());
        onView(withId(R.id.body)).perform(typeText("P1"),
                closeSoftKeyboard());



        onView(withId(R.id.submit)).perform(click());



    }



    @Test
    public void changUserName () {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("settings")).perform(click());


//        onView(withId(R.id.Addtask)).perform(click());
//        // Type text and then press the button.
       onView(withId(R.id.username)).perform(typeText("1"),
               closeSoftKeyboard());
        onView(withId(R.id.btn_submit)).perform(click());
        onView(isRoot()).perform(ViewActions.pressBack());


        onView(withId(R.id.Uname)).check(matches(withText("1")));

//        onView(withId(R.id.body)).perform(typeText("1"),
//                closeSoftKeyboard());
//
//
//
//        onView(withId(R.id.submit)).perform(click());





    }





}




