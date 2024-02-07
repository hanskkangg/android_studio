package algonquin.cst2335.kang0057.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<String> editTextData = new MutableLiveData<>();

    public MutableLiveData<String> getEditTextData() {
        return editTextData;
    }
}
