package com.imooc.tab03;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener
{
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments;

	private LinearLayout mTabShot;
	private LinearLayout mTabFind;
	private LinearLayout mTabSetting;

	private ImageButton mImgShot;
	private ImageButton mImgFind;
	private ImageButton mImgSetting;

	private TextView mTextShot;
	private TextView mTextFind;
	private TextView mTextSetting;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
	}

	private void initEvent()
	{
		mTabShot.setOnClickListener(this);
		mTabFind.setOnClickListener(this);
		mTabSetting.setOnClickListener(this);
	}

	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		mTabShot = (LinearLayout) findViewById(R.id.id_tab_shot);
		mTabFind = (LinearLayout) findViewById(R.id.id_tab_find);
		mTabSetting = (LinearLayout) findViewById(R.id.id_tab_setting);

		mImgShot = (ImageButton) findViewById(R.id.id_tab_shot_img);
		mImgFind = (ImageButton) findViewById(R.id.id_tab_find_img);
		mImgSetting = (ImageButton) findViewById(R.id.id_tab_setting_img);

		mTextShot = (TextView)findViewById(R.id.id_tab_shot_text);
		mTextFind = (TextView)findViewById(R.id.id_tab_find_text);
		mTextSetting = (TextView)findViewById(R.id.id_tab_setting_text);

		mFragments = new ArrayList<Fragment>();
		Fragment mTab01 = new ShotFragment();
		Fragment mTab02 = new FindFragment();
		Fragment mTab03 = new SettingFragment();
		mFragments.add(mTab01);
		mFragments.add(mTab02);
		mFragments.add(mTab03);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{

			@Override
			public int getCount()
			{
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mFragments.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int arg0)
			{
				int currentItem = mViewPager.getCurrentItem();
				setTab(currentItem);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.id_tab_shot:
				setSelect(0);
				break;
			case R.id.id_tab_find:
				setSelect(1);
				break;
			case R.id.id_tab_setting:
				setSelect(2);
				break;
			default:
				break;
		}
	}

	private void setSelect(int i)
	{
		setTab(i);
		mViewPager.setCurrentItem(i);
	}

	private void setTab(int i)
	{
		resetImgs();
		// 设置图片为亮色
		// 切换内容区域
		switch (i)
		{
			case 0:
				mImgShot.setImageResource(R.drawable.tab_shot_blue);
				mTextShot.setTextColor(this.getResources().getColor(R.color.tabTextBlue));
				break;
			case 1:
				mImgFind.setImageResource(R.drawable.tab_compass_blue);
				mTextFind.setTextColor(this.getResources().getColor(R.color.tabTextBlue));
				break;
			case 2:
				mImgSetting.setImageResource(R.drawable.tab_person_blue);
				mTextSetting.setTextColor(this.getResources().getColor(R.color.tabTextBlue));
				break;
		}
	}

	/**
	 * 切换图片至暗色
	 */
	private void resetImgs()
	{
		mImgShot.setImageResource(R.drawable.tab_shot_gray);
		mImgFind.setImageResource(R.drawable.tab_compass_gray);
		mImgSetting.setImageResource(R.drawable.tab_person_gray);

		mTextShot.setTextColor(this.getResources().getColor(R.color.tabTextGray));
		mTextFind.setTextColor(this.getResources().getColor(R.color.tabTextGray));
		mTextSetting.setTextColor(this.getResources().getColor(R.color.tabTextGray));

	}

}
