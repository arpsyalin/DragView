# DragView
这是一款很简单易用的拖拽控件，看到同事iOS底部自带抽拉的控件自带拖拽效果，而android的抽屉控件个人觉得用起来略显重量，于是写了一个简单易用的拖拽控件。

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle 

	allprojects {
		repositories { 
			maven { url 'https://www.jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.arpsyalin:DragView:v1.0'
	}

Step 3. use DragView
 
 <com.lyl.dragview.DragView
        android:layout_width="200dp"
        android:layout_height="wrap_content">

     <!-- 添加一个View(ImageView 或者 Button ...) 或者一个ViewGroup(LinearLayout 或 RelativebLayout等 )-->

    </com.lyl.dragview.DragView>
    
DragView 可以设置一些属性
app:minShowSize="50dp" 最小显示的大小 （必须设置默认不设置则为0会出现什么情况我没测,建议这个值比DragView内包含的这个布局要大，当然具体设置请自己尝试后取最佳效果）
app:showModel="max" 初始显示为最大  或者 app:showModel="min" 初始显示为最小
app:dragModel
包含4个枚举:Bottom2Up(可以从最小值往上拖拽到最大) Up2Bottom(可以从最小值往下拖拽到最大) Right2Left(可以从最小值往左拖拽到最大) Left2Right(可以从最小值往右拖拽到最大)
