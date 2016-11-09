# SwipeDelMenuLayout
[![](https://jitpack.io/v/mcxtzhang/SwipeDelMenuLayout.svg)](https://jitpack.io/#mcxtzhang/SwipeDelMenuLayout)

相关博文：
http://blog.csdn.net/zxt0601/article/details/52303781

喜欢随手点个star 多谢 
## 作者相关：

我的CSDN博客：

http://blog.csdn.net/zxt0601

***
# 重要的话 开头说，not for the RecyclerView or ListView, for the Any ViewGroup.
本控件不依赖任何父布局，不是针对 RecyclerView、ListView，而是任意的ViewGroup里的childView都可以使用侧滑(删除)菜单。

使用极其方便，**没有任何耦合性** ,直接作为某个ViewGroup的Item的根布局即可。

# 效果一览：

LinearLayout:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/linear.gif)

GridLayoutManager:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/grid.gif)

DoubleSwipe(双向滑动):

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/doubleSwipe.gif)

LinearLayoutManager:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/LinearLayoutManager1.gif)

iOS interaction :

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/iOS.gif)


# 使用：
Step 1. 在项目根build.gradle文件中增加JitPack仓库依赖。
```
    allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
Step 2. Add the dependency
```
	dependencies {
	        compile 'com.github.mcxtzhang:SwipeDelMenuLayout:V1.1.0'
	}
```


Step 3. 在需要侧滑删除的Item外面套上本控件：
```
<?xml version="1.0" encoding="utf-8"?>
<com.mcxtzhang.swipemenulib.SwipeMenuLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="100dp"
    android:clickable="true"
    android:paddingBottom="1dp">

    <TextView
        android:id="@+id/content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="?android:attr/selectableItemBackground"
        android:gravity="center"
        android:text="项目中我是任意复杂的原Item布局"/>

    <!-- 以下都是侧滑菜单的内容依序排列 -->
    <Button
        android:id="@+id/btnTop"
        android:layout_width="60dp"
        android:layout_height="match_parent"
        android:background="#d9dee4"
        android:text="置顶"
        android:textColor="@android:color/white"/>

    <Button
        android:id="@+id/btnUnRead"
        android:layout_width="120dp"
        android:layout_height="match_parent"
        android:background="#ecd50a"
        android:clickable="true"
        android:text="标记未读"
        android:textColor="@android:color/white"/>

    <Button
        android:id="@+id/btnDelete"
        android:layout_width="60dp"
        android:layout_height="match_parent"
        android:background="@color/red_ff4a57"
        android:text="删除"
        android:textColor="@android:color/white"/>

</com.mcxtzhang.swipemenulib.SwipeMenuLayout>

```

至此 您就可以使用高仿IOS、QQ 侧滑删除菜单功能了

Step 4.(可选optional)
如果不想要IOS阻塞式交互效果，或者想打开右滑菜单功能。
```
((SwipeMenuLayout) holder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false);//这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
```

**另外**，
201609012补充：

在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时关闭，

将ItemView强转成CstSwipeDelMenu，并调用quickClose()。

如：
((CstSwipeDelMenu) holder.getConvertView()).quickClose(); 


推荐使用RecyclerView， 

在RecyclerView中，如果删除时，建议使用mAdapter.notifyItemRemoved(pos)，

否则删除没有动画效果， 且如果想让侧滑菜单同时关闭，也需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();


###更新日志###
2016 11 09 更新：
1 适配GridLayoutManager，将以第一个子Item(即ContentItem)的宽度为控件宽度。
2 使用时，如果需要撑满布局，切记第一个子Item(Content)，宽度要是match_parent.

2016 11 04 更新：
1 优化了长按事件和侧滑事件的关系，尽量的参考QQ。

2016 11 03 更新：
1 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件（和QQ交互一样）

2016 10 21 更新：
1 当父控件宽度不是全屏时的bug。
2 仿QQ，侧滑菜单展开时，点击除侧滑菜单之外的所有区域包括菜单的内容部分，关闭侧滑菜单。

2016 10 21 更新：
1 增加viewChache 的 get()方法，可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。

2016 09 30 更新：
1 支持多向滑动：
![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/doubleSwipe.gif)

2016 09 28 更新点：
1 增加一个item点击事件设置的示例。

2016 09 12 更新点：
1 增加用RecyclerView、ListView实现的完整删除Demo 供不会用的同学参考。
2 增加一个quickClose()方法，更好的在ListView中使用，不过还是推荐大家用RecyclerView。





***
【1 序言】
侧滑删除的轮子网上有很多，最初在github上看过一个，还是ListView时代，那是一个自定义ListView 实现侧滑删除的，当初就觉得这种做法不是最佳，万一我项目里又同时有自定义ListView的需求，会增加复杂度。

写这篇文章之前又通过毒度搜了一下，排名前几的CSDN文章，都是通过自定义ListVIew和ViewGropup实现的滑动删除。

况且现在是RecyclerView时代，难不成我要把那些代码再自定义RecyclerView写一遍么。

我想说No，网上大多数的做法代码侵入性太强，尽量不要继承 ListVIew 做什么事，换成 RecyclerView 呢，扩展性太局限了，

本文的做法只要在 Item 的根布局换成这个 自定义ViewGroup 即可，完全不 care 你用 RecyclerView 还是 ListVIew，耦合性为 0


听说隔壁iOS 侧滑删除是一个系统自带的控件，那么我们Android党能否也自定义一个ViewGroup控件，然后一劳永逸，每次简单拿来用就好了呢?

自定义ViewGroup实现侧滑删除简单，难得是还要同时 处理多指滑动的屏蔽，防止两个侧滑菜单同时出现，等等，

有办法将这些东西都用一个ViewGroup搞定么？

看本文如何巧用static类变量来解决这些矛盾冲突。

【2 功能预览】：

包含且不仅包含以下功能

1 侧滑拉出菜单。

2 点击除了这个item的其他位置，菜单关闭。

3 侧滑过程中，不许父控件上下滑动。

4 多指同时滑动，屏蔽后触摸的几根手指。

5 不会同时展开两个侧滑菜单。

6 侧滑菜单时 拦截了长按事件。

7 侧滑时，拦截了点击事件

8 通过开关 isLeftSwipe支持左滑右滑
【3 使用预览】

需要效果一 ：//((CstSwipeDelMenu)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果

需要效果二 ：直接使用，可能大部分公司比较钟爱IOS效果，我忍痛默认IOS


```
<?xml version="1.0" encoding="utf-8"?>
<mcxtzhang.swipedelmenu.view.CstSwipeDelMenu xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:clickable="true">

    <TextView
        android:id="@+id/content"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:gravity="center"
        android:text="Content布局，项目中替换成你原来的Item布局" />

    <Button
        android:id="@+id/btnDelete"
        android:layout_width="60dp"
        android:layout_height="match_parent"
        android:background="@color/red_ff4a57"
        android:text="删除" />

    <Button
        android:id="@+id/update"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:clickable="true"
        android:text="更新" />

    <RelativeLayout
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:background="@color/red_ff4a57"
        android:clickable="true">

        <TextView
            android:id="@+id/tv_delete"
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:layout_centerVertical="true"
            android:drawablePadding="5dp"
            android:drawableTop="@drawable/point_icon_delete"
            android:gravity="center"
            android:text="删除"
            android:textColor="@android:color/white" />
    </RelativeLayout>

</mcxtzhang.swipedelmenu.view.CstSwipeDelMenu>
```

就这么简单，
只需要在 侧滑删除的item的layout的xml里，将父控件换成我们的自定义ViewGroup即可。

第一个子View放置item的内容即可(正式项目里一般是一个ViewGroup)，

从第二个子View开始，是我们的侧滑菜单区域，如我们的demo图，是三个Button。

```
//注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
(holder.content).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(mContext, ""+mDatas.get(position).name, Toast.LENGTH_SHORT).show();
    }
});
```


