# MonkeyDatePager
自定义控件，用于展示不同时段下的不同时间

## 依赖使用
1.在build.gradle(project)中：
```
allprojects {
		repositories {
			......
			maven { url "https://jitpack.io" }
		}
	}
```
2.在build.gradle(app)中：
```
dependencies {
    ......
	  compile 'com.github.mayemonkey:MonkeyDatePager:1.5.0'
}
```

## 公开方法
```getTime```                           获取当前显示的时间值Calendar
```getType```                           获取当前显示的时段
```getInnerTime```                      获取当前控件所代表的时间
```setMonkeyOnTypeChangedListener```    设置时段改变监听
```setMonkeyOnTimeChangedListener```    设置时间改变监听

##使用示例

```
MonkeyDatePager mdp_home = (MonkeyDatePager) findViewById(R.id.mdp_home);
mdp_home.setOnMonkeyTypeChangedListener(new OnMonkeyTypeChangedListener(){
    public void onTypeChanged(int type){
    
    }
});

mdp_home.setOnMonkeyTimeChangedListener(new OnMonkeyTimeChangedListener(){
    public void onTimeChanged(Calenadr time){
        //此处的Calendar参数需要根据type进行处理
    }
});
```

##感谢
[AutoFitTextView](https://github.com/grantland/android-autofittextview)

