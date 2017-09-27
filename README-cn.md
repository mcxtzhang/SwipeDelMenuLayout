# SwipeDelMenuLayout
[![](https://jitpack.io/v/mcxtzhang/SwipeDelMenuLayout.svg)](https://jitpack.io/#mcxtzhang/SwipeDelMenuLayout)

相关博文：
从0实现V1.0版本
http://blog.csdn.net/zxt0601/article/details/52303781 

V1.2版本的更新和改动以及使用
http://blog.csdn.net/zxt0601/article/details/53157090

喜欢随手点个star 多谢 
##  在哪里找到我：
我的github：

https://github.com/mcxtzhang

我的CSDN博客：

http://blog.csdn.net/zxt0601

我的稀土掘金：

http://gold.xitu.io/user/56de210b816dfa0052e66495

我的简书：

http://www.jianshu.com/users/8e91ff99b072/timeline

***
# 重要的话 开头说，not for the RecyclerView or ListView, for the Any ViewGroup.
本控件**不依赖任何**父布局，不是针对 RecyclerView、ListView，而是**任意的ViewGroup**里的childView都可以使用侧滑(删除)菜单。

# 概述

本控件从撸出来在项目使用至今已经过去7个月，距离第一次将它push至github上，也已经2月+。（之前，我发表过一篇文章。传送门:http://gold.xitu.io/entry/57d1115dbf22ec005f9593c6/detail, 里面详细描述了本控件V1.0版本是怎么实现的。）


期间有很多朋友在评论、issue里提出了一些**改进**意见，例如支持设置滑动方向（左右）、高仿QQ的交互、支持GridLayoutManager等，以及一些**bug**。已经被我**全部实、修复**。并且将其打包至jitpack，引入**更方便**。和第一版相比，改动挺多的。故将其整理，新发一版。

那么本文先从如何使用它讲起，然后介绍它包含的特性、支持的属性。最后就几个难点和冲突的解决进行讲解。

代码传送门：喜欢的话，随手点个star。多谢
https://github.com/mcxtzhang/SwipeDelMenuLayout

先上几个gif给各位看官感受一下最新版的魅力（以下版本都顺便展示了可选的双向滑动）

本控件最大魅力就是0耦合,所以先上配合我另一个库组装的效果(ItemDecorationIndexBar + SwipeMenuLayout)：
(ItemDecorationIndexBar : https://github.com/mcxtzhang/ItemDecorationIndexBar)

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/ItemDecorationIndexBar_SwipeDel.gif)

随便来个流式布局也不在话下：

![](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/FlowSwipe.gif)


Android Special Version （无阻塞式，侧滑菜单展开时，依然可以展开其他侧滑菜单，同时上一个菜单会自动关闭）:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/LinearLayoutManager1.gif)

GridLayoutManager （和上图的代码比，只需修改RecyclerView的LayoutManager。）:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/grid.gif)

LinearLayout （不需任何修改，连LinearLayout也可以简单的实现侧滑菜单）:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/linear.gif)

iOS interaction （阻塞式交互，高仿QQ，侧滑菜单展开式，屏蔽其他ITEM所有操作）:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/iOS.gif)

在ViewPager中使用：
![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/viewpager.gif)





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
	        compile 'com.github.mcxtzhang:SwipeDelMenuLayout:V1.3.0'
	}
```


Step 3. 在需要侧滑删除的ContentItem外面套上本控件，在本控件内依次排列ContentItem、菜单即可：

**至此 您就可以使用高仿IOS、QQ 侧滑删除菜单功能了**
（侧滑菜单的点击事件等是通过设置的id取到，与其他控件一致，不再赘述）

Demo里，我的ContentItem是一个TextView，那么我就在其外嵌套本控件，并且以侧滑菜单出现的顺序，依次排列菜单控件即可。
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
        android:text="项目中我是任意复杂的原ContentItem布局"/>

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

**注意事项**：
* 若是在ListView、RecyclerView中使用，点击事件正确的设置应该是在 Adapter  里对 ContentItem 设置，不能使用listview.setOnItemClickListener。
因为此时 Item 是本控件了，不是里面的 ContentItem 那块区域了，且本控件区域有很多触摸的判断，内部包含 ContentItem 和侧滑菜单 Menu。

* 在ListView中使用，
在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时关闭，

将ItemView强转成CstSwipeDelMenu，并调用quickClose()。

如：
((CstSwipeDelMenu) holder.getConvertView()).quickClose(); 


推荐使用RecyclerView， 

在RecyclerView中，如果删除时，建议使用mAdapter.notifyItemRemoved(pos)，

否则删除没有动画效果， 且如果想让侧滑菜单同时关闭，也需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();


* 在ViewPager中使用：
用`CstViewPager`替换`ViewPager`，解决滑动冲突
```
    <com.mcxtzhang.swipemenulib.CstViewPager
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"></com.mcxtzhang.swipemenulib.CstViewPager>
```
---

# 支持属性：
1 通过 isIos 变量控制是否是IOS阻塞式交互，默认是打开的。
2 通过 isSwipeEnable 变量控制是否开启右滑菜单，默认打开。（某些场景，复用item，没有编辑权限的用户不能右滑）
3 通过开关 isLeftSwipe支持左滑右滑

有两种方式设置：
一：xml：

```xml
<com.mcxtzhang.swipemenulib.SwipeMenuLayout
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:ios="false"
    app:leftSwipe="true"
    app:swipeEnable="true">
```

二： java代码：
```java
//这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑  禁用掉侧滑菜单
((SwipeMenuLayout) holder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false).setSwipeEnable(false);
```

# 支持特性：

* 不会同时展开2+个侧滑菜单。（可见界面上最多只会出现一个侧滑菜单）。
* 侧滑过程中，禁止父控件上下滑动。
* 多指同时滑动，屏蔽后触摸的几根手指。
* 增加viewChache 的 get()方法，可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。
* 以第一个子Item(即ContentItem)的宽度为控件宽度


# 每次更新的checklist：
由于持续迭代，会发生完成一个feature、fix一个bug后，导致新的bug。
so，整理一份checkList，供每次迭代后验证，都通过，才会push到github库上。

项目 | 备注 | 验证
--- |----------| ---
isIos | 切换至IOS阻塞交互模式、Android特色无阻塞交互模式 以下feature都可正常工作|
isSwipeEnable | 是否支持关闭侧滑功能
isLeftSwipe | 是否支持双向滑动
ContentItem内容可单击 | 
ContentItem内容可长按 |
侧滑菜单显示时，ContentItem不可点击 |
侧滑菜单显示时，ContentItem不可长按 |
侧滑菜单显示时，侧滑菜单可以点击 |
侧滑菜单显示时，点击ContentItem区域关闭菜单 |
侧滑过程中，屏蔽长按事件 |
通过滑动关闭菜单，不应该触发ContentItem点击事件 |


---

###更新日志###
2017 09 27更新：
* 解决 滑动冲突的 ViewPager：CstViewPager
因为ViewPager 和 SwipMenuLayout都是水平方向滑动的控件。所以在一起使用时会有冲突，使用CstViewPager，可以在ViewPager的第一页使用左滑。在ViewPager的最后一页使用右滑菜单。

2016 12 09更新：
* ListView快速滑动快速删除时，偶现菜单不消失的bug。

2016 12 07 更新：
 * 禁止侧滑时(isSwipeEnable false)，点击事件不受干扰。
 
2016 11 14 更新：
 * 支持padding,且后续计划加入上滑下滑，因此不再支持ContentItem的margin属性。
 * 修改回弹的动画，更平滑。
 * 微小位移的move不回回弹的bug

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



---
