package si.lukag.featureflagsdemo.ui.plus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlusViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> joke;

    public PlusViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This Plus fragment is seen only when plus_content is set to 1. Here is a joke for you!");

        joke = new MutableLiveData<>();
        joke.setValue(
                "<p>My dog ate my computer science project.</p>" +
                "<p>Professor: Your dog ate your coding assignment?</p>" +
                "<p>It took him a couple bytes.</p>");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getJoke() {
        return joke;
    }
}