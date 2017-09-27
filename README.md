# SwipeDelMenuLayout
[![](https://jitpack.io/v/mcxtzhang/SwipeDelMenuLayout.svg)](https://jitpack.io/#mcxtzhang/SwipeDelMenuLayout)

#### [中文版文档](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/README-cn.md)

Related blog:
V1.0:
http://blog.csdn.net/zxt0601/article/details/52303781 

V1.2:
http://blog.csdn.net/zxt0601/article/details/53157090

If you like,please give me a star, thank you very much
##  Where to find me：
Github：

https://github.com/mcxtzhang

CSDN：

http://blog.csdn.net/zxt0601

gold.xitu.io：

http://gold.xitu.io/user/56de210b816dfa0052e66495

jianshu：

http://www.jianshu.com/users/8e91ff99b072/timeline

***
# Important words: not for the RecyclerView or ListView, for the Any ViewGroup.

# Intro

This control has since rolled out in the project use over the past seven months, distance on a push to making it the first time, also has + 2 month. (before, I published an article. Portal: http://gold.xitu.io/entry/57d1115dbf22ec005f9593c6/detail, it describes in detail the control how V1.0 version is done.)
During a lot of friends in the comment, put forward some improvement of ** in the issue, such as support setting sliding direction (or so), high imitation QQ interaction, support GridLayoutManager etc, as well as some bug **. I have been all real, repair **. And its packaging to jitpack, introducing more convenient**. Compared to the first edition, change a lot. So to arrange, new version.
So this paper start with how to use it, and then introduces the features of it contains, in support of the property. Finally a few difficulties and conflict resolution.

ItemDecorationIndexBar + SwipeMenuLayout
(The biggest charm is 0 coupling at the controls,So, you see first to cooperate with me another library assembly effect)：
(ItemDecorationIndexBar : https://github.com/mcxtzhang/ItemDecorationIndexBar)

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/ItemDecorationIndexBar_SwipeDel.gif)

Casually to use in a flow layout also easy:

![](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/FlowSwipe.gif)


Android Special Version （Without blocking type, when the lateral spreads menus, still can be expanded to other side menu, at the same time on a menu will automatically shut down）:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/LinearLayoutManager1.gif)

GridLayoutManager （And the above code than, need to modify RecyclerView LayoutManager）:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/grid.gif)

LinearLayout （Without any modification, even can simple LinearLayout implementation side menu）:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/linear.gif)

iOS interaction （Block type interaction, high imitation QQ, sideslip menu expansion, blocking other ITEM all operations）:

![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/iOS.gif)

use in ViewPager：
![image](https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/viewpager.gif)




# Usage：
Step 1. Add the JitPack repository to your build file。
Add it in your root build.gradle at the end of repositories:
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


Step 3. Outside the need sideslip delete ContentItem on the controls, within the control lined ContentItem, menu：
**At this point You can use high copy IOS, QQ sideslip delete menu functions**
（Sideslip menu click events is by setting the id to get, in line with other controls, no longer here）

Demo, I ContentItem is a TextView, then I'm in the outside its nested controls, and order, in the side menu, in turn, can arrange menu controls.
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

**One Tips**：
If it is used in the ListView, RecyclerView, click event Settings should be correct in the Adapter for ContentItem Settings, cannot use ListView. SetOnItemClickListener.
When the Item is control, not the ContentItem inside the area, and there are a lot of touch judge the control area, internal contain ContentItem and sideslip Menu Menu.


---

# Attributes：
1 Through isIos variable control whether IOS block type interaction, is on by default.
2 Through isSwipeEnable variable control whether open right menu, open by default. (in some scenarios, reuse item, no edit permissions the user cannot slide from right)
3 Through the left slide right slide switch isLeftSwipe support

how to setting：
One：xml：

```xml
<com.mcxtzhang.swipemenulib.SwipeMenuLayout
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:ios="false"
    app:leftSwipe="true"
    app:swipeEnable="true">
```

Other： java Codes：
```java
//这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑  禁用掉侧滑菜单
((SwipeMenuLayout) holder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false).setSwipeEnable(false);
```

# Speciality：

* don't simultaneously 2 + a side menu. (visible interface will appear, at most, only a side menu).
* in the process of sideslip, banning parent slide up and down.
* more refers to slide at the same time, the screen after the touch of a few fingers.
* increase viewChache the get () method, which can be used in: when click on the external space, shut down is the slide of the menu.
* to the first child Item (i.e. ContentItem) to control the width of the width


# checklist：
Will happen due to the last iteration, after completing a feature, fix a bug that caused new bug.
So, to sort out a checkList for validation after each iteration, all through, will push to making library.

feature | desc | verify 
--- |----------| ---
isIos | Switch to the IOS obstruction interaction patterns, Android features non-blocking feature under interactive mode can work normally|
isSwipeEnable |Whether to support close function of sideslip
isLeftSwipe | Whether to support two-way sliding
Click the ContentItem content | 
ContentItem content can be long press |
Sideslip menu display, ContentItem not click |
Sideslip menu is displayed, ContentItem not long press  |
Lateral spreads menu is displayed, sideslip can click on the menu  |
Sideslip menu is displayed, click ContentItem area close the menu  |
Lateral spreads, in the process of shielding long press event  |
By sliding off the menu, should not trigger ContentItem click event  |


**In addition**，
In a ListView, click on the menu of sideslip options, if you want the sideslip menu closed at the same time,
Will into CstSwipeDelMenu ItemView is strong, and call the `quickClose()`.
Such as:
`((CstSwipeDelMenu) holder. GetConvertView ()). QuickClose ();`
It is recommended to use RecyclerView,
In RecyclerView, if deleted, it is recommended to use mAdapter. NotifyItemRemoved (pos),
Or delete no animation effects, and if you want to let the sideslip menu closed at the same time, also need to call at the same time `((CstSwipeDelMenu) holder. ItemView). QuickClose ();`

---

###Update log###
2017 09 27 update:
* solving sliding conflicts in ViewPager:CstViewPager
Because ViewPager and SwipMenuLayout are horizontal sliding controls. So, when used together, there will be conflicts. Using CstViewPager, you can use left slider on the first page of ViewPager. Use the right click menu on the last page of ViewPager.

2016 12 07 update：
 * Fix a bug :when using ListView，quick swipe and quick click del menu, next Item is Swiped.。
 
2016 12 07 update：
 * When the isSwipeEnable is false，the click event of contentItem is undisturbed。
 
2016 11 14 update:
* support the padding, and the subsequent slide down on plans to join, so no longer support ContentItem margin properties.
* modify the springback of animation, more smooth.
* tiny displacement of the move does not rebound bug back

2016 11 09 update:
1 adapter GridLayoutManager, will be the first child Item (i.e. ContentItem) to control the width of the width.
2 when using, if you need to support full layout, remember that the first child Item (Content), if the width match_parent.

2016 11 04 update:
1 long was optimized according to the relationship between events and sideslip, as far as possible reference to QQ.

2016 11 03 update:
1 determine the starting point finger, if the distance to slide, shielding all the click event (like QQ interaction)

2016 10 21 update:
1 when the parent controls when the width is not full screen bug.
2 imitation QQ, sideslip menus, click on all regions except the sideslip menu includes the contents of the menu, close the side menu.

2016 10 21 update:
1 increase viewChache the get () method, which can be used in: when click on the external space, shut down is the slide of the menu.

2016 09 30 update:
1 support for slide.
! [image] (https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/gif/doubleSwipe.gif)

2016-09 28 update site:
Add an item 1 click event set example.

2016-09 12 update site:
1 increase with RecyclerView, ListView can delete the complete Demo for not using classmates reference.
2 add a quickClose () method, better use in the ListView, but still recommend use RecyclerView.



---
