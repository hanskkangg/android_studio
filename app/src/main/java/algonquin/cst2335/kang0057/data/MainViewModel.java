package algonquin.cst2335.kang0057.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<String> editTextData = new MutableLiveData<>();
    private MutableLiveData<Boolean> coffeePreference = new MutableLiveData<>();

    public MutableLiveData<String> getEditTextData() {
        return editTextData;
    }

    public MutableLiveData<Boolean> getCoffeePreference() {
        return coffeePreference;
    }
}
