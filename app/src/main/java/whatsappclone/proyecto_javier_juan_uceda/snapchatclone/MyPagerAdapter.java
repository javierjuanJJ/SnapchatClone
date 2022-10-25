package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.Fragment.CameraFragment;
import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.Fragment.ChatFragment;
import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.Fragment.StoryFragment;

class MyPagerAdapter extends FragmentPagerAdapter {

    private static final Fragment[] fragments = {ChatFragment.newInstance(), CameraFragment.newInstance(), StoryFragment.newInstance()};

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
