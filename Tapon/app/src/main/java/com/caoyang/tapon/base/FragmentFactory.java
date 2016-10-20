package com.caoyang.tapon.base;


/**
 * 不需要的参数的fragment都可以使用此方法，尽量减少activity的创建
 */
public class FragmentFactory {
    //fragment的类型
    public static final String FRAGMENT_TYPE = "fragment_type";
    //我的账号
    public static final String FRAGMENT_ACCOUNT = "fragment_account";


    public static BaseFragment getFragment(String fragmentName) {
        switch (fragmentName) {
            case FRAGMENT_ACCOUNT:
//                return MyAccountFragment.newInstance();
        }
        return null;
    }
}
