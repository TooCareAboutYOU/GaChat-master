前提：由于开发时间非常紧张，所以未曾优化项目整体架构
1、目前整体数据框架使用的是MVP基本模式，（若后续有时间，会使用代理模式或者双重代理模式）；
2、所有用户数据请求、更新的地方，后续会使用观察者模式优化；
3、不需要实时获取用户信息的地方，后续将改用 原型模式
4、Base基类使用的模板模式


 <!--<FrameLayout-->
        <!--android:id="@+id/containerView"-->
        <!--android:layout_width="match_parent"-->
        <!--android:layout_height="match_parent"-->
        <!--android:layout_margin="10dp"/>-->


    <!--<ProgressBar-->
        <!--android:id="@+id/progressBarView"-->
        <!--style="?android:attr/progressBarStyle"-->
        <!--android:layout_width="wrap_content"-->
        <!--android:layout_height="wrap_content"-->
        <!--android:layout_gravity="center"-->
        <!--android:visibility="gone"/>-->

    <!--<com.mingle.widget.LoadingView-->
        <!--android:layout_width="match_parent"-->
        <!--android:layout_height="match_parent"/>-->

    <!--<pl.tajchert.waitingdots.DotsTextView-->
        <!--android:layout_width="wrap_content"-->
        <!--android:layout_height="wrap_content"-->
        <!--android:layout_gravity="center"-->
        <!--android:gravity="center"-->
        <!--android:textSize="45dp"-->
        <!--android:text="Loading"-->
        <!--android:textColor="@color/colorGray"-->
        <!--dots:autoplay="true"-->
        <!--dots:period="1000"/>-->