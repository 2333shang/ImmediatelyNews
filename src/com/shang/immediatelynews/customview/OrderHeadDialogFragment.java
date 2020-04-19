package com.shang.immediatelynews.customview;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.OrderHeadViewpagerAdapter;
import com.shang.immediatelynews.entities.Order;
import com.shang.immediatelynews.fragment.OrderHeadFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

@ContentView(R.layout.owner_head_fragment_layout)
public class OrderHeadDialogFragment extends DialogFragment {

	@ViewInject(R.id.order_head_viewpager)
	private ViewPager viewPager;
	private List<Order> orders;
	private int position;
	
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.OrderHeadDialog);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }
    
    public OrderHeadDialogFragment() {
		super();
	}
    
    public OrderHeadDialogFragment(List<Order> orders, int position) {
		super();
		this.orders = orders;
		this.position = position;
	}

	@Override
    public void onActivityCreated(Bundle arg0) {
    	super.onActivityCreated(arg0);
    	Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        //设置dialog的背景颜色，我们可以直接在XML中给dialog加一个背景色，如果不设置背景颜色就会造成背景是透明的
//        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得窗体的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        //设置Dialog宽度匹配屏幕宽度
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置Dialog高度自适应
        lp.height = 600;
        //将属性设置给窗体
        window.setAttributes(lp);
    	List<OrderHeadFragment> fragments = getFragments();
        OrderHeadViewpagerAdapter orderHeadAdapter = new OrderHeadViewpagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(orderHeadAdapter);
        viewPager.setCurrentItem(position);
    }
    
    private List<OrderHeadFragment> getFragments(){
        List<OrderHeadFragment> orderFragments = new ArrayList<OrderHeadFragment>();
        for(int i=0; i<orders.size()-1 ; i++) {
        	orderFragments.add(new OrderHeadFragment(orders.get(i), i));
        }
        return orderFragments;
    }
}
