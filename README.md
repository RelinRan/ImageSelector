# ImageSelector
Android图片原生选择器（Android Image native selector）
# 效果图
![img](https://github.com/RelinRan/ImageSelector/blob/master/image_selector.png)
## 方法一  ARR依赖
[ImageSelector.arr](https://github.com/RelinRan/ImageSelector/blob/master/ImageSelector.aar)
Method 1 ARR dependence
```
android {
    ....
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
}

dependencies {
    implementation(name: 'AndroidKit', ext: 'aar')
}

```

## 方法二   JitPack依赖
Method 2 JitPack dependencies
### A.项目/build.grade
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### B.项目/app/build.grade
```
	dependencies {
	        implementation 'com.github.RelinRan:ImageSelector:1.0.4'
	}
```


## Application配置

### 继承Application(必须),如果自己有Application就在自己的Application的onCreate()方法里面初始化即可。
Inherit Application(must) and, if you have an Application, initialize it in your Application's onCreate() method.
```
public class XXXApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化图片选择器
        ImageSelector.init();
    }

}
```
### B.color.xml
工具类可以配置颜色，在自己工程颜色加如下颜色就可以了。如果不需要修改颜色使用默认颜色可以忽略。
Tool class can configure the color, add the following color to your project color. If you do not need to change the color use the default color can be ignored.
```
    <!--背景颜色-->
    <color name="colorImageSelectorBackground">#FFFFFF</color>
    <!--分割线颜色-->
    <color name="colorImageSelectorDivider">#EFEFEF</color>
    <!--文字颜色-->
    <color name="colorImageSelectorMenuText">#434444</color>
    <!--取消文字颜色-->
    <color name="colorImageSelectorCancelText">#434444</color>
    <!--按钮按下颜色-->
    <color name="colorImageSelectorButtonUnpressed">#FFFFFF</color>
    <!--按钮正常颜色-->
    <color name="colorImageSelectorButtonPressed">#F9F9F9</color>
```

## Activity使用
```
        ImageSelector.Builder builder = new ImageSelector.Builder(activity);
        //是否剪切
        builder.crop(false);
        //剪切宽高比
        builder.aspectX(2);
        builder.aspectY(1);
        //是否压缩
        builder.compress(true);
        //压缩大小300kb以下
        builder.size(300);
        builder.listener(new OnImageSelectListener() {
            @Override
            public void onImageSelectSucceed(Uri uri) {
                File file = IOUtils.decodeUri(activity,uri);
            }

            @Override
            public void onImageSelectFailed(String msg) {

            }
        });

        //一下是扩展项，一般情况可以不要
        //是否修改原来菜单的逻辑，如果为true,拍照和相册的逻辑自己写,在onImageSelectMenu（）里面判断。
        builder.defineIntent(true);
        builder.menu(new String[]{"测试"});//增加菜单项
        builder.menuListener(new OnImageSelectMenuListener() {
            @Override
            public void onImageSelectMenu(ImageSelector selector,List<String> list, int position) {
                //如果自定义了菜单的逻辑，工具支持直接调用选择相册方法和拍照方法
                selector.startCameraActivity();//拍照
                selector.startGalleryActivity();//相册
                //处理以上的结果，需要在对应Activity/Fragment里面 selector.onActivityResult(int requestCode, int resultCode, Intent data),
                //调用 selector.onActivityResult（）只有的结果会在builder.listener设置的OnImageSelectListener回调里面
            }
        });

       //构建显示,如果不在构建的时候显示就传递false
      ImageSelector selector = builder.build(true);


    //必须设置
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (selector != null) {
            selector.onActivityResult(requestCode, resultCode, data);
        }
    }

```
