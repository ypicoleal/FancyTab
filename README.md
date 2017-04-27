# Fancy Tab

A Tab layout inspired by the two shots ["Scene Club Tickets. Interaction"](https://dribbble.com/shots/3442319-Scene-Club-Tickets-Interaction) and ["Two Invites App"](https://dribbble.com/shots/3359570-Two-Invites-App) by [Yaroslav Zubko](https://dribbble.com/Yar_Z)

Demo:

![alt text](https://github.com/ypicoleal/FancyTab/raw/master/media/demo1.gif "Demo gif") ![alt text](https://github.com/ypicoleal/FancyTab/raw/master/media/demo2.gif "Demo gif")

## Usage


Add to your Android layout xml:
```xml
<com.github.ypicoleal.fancytablayout.FancyTabLayout
            android:id="@+id/tabs"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:tabFormat="onlyText" />
```

The options for the ```tabFormat ``` are ```onlyText```, ```imgTitle``` and ```onlyImg```

Then in your activity bound it with your viewPager
```java
FancyTabLayout tabLayout = (FancyTabLayout) findViewById(R.id.tabs);
tabLayout.setupWithViewPager(mViewPager);
```

If you are going to use the options ```imgTitle``` or ```onlyImg``` for ```tabFormat ``` your ```FragmentPagerAdapter ``` class will have to extend from ```FancyFragmentPageAdapter ``` eg:

```java
    private class SectionsPagerAdapter extends FancyFragmentPageAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object getPageImageURL(int position) {
            //your code about the url of the image
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "PAGE " + (position + 1)
        }
    }
```

And set the ```ImageLoader``` for the layout:
```java
tabLayout.setImageLoader(new FancyTabLayout.ImageLoader() {
    @Override
    public void loadImage(ImageView view, Object url) {
        Picasso.with(MainActivity.this)
               .load((String) url)
               .placeholder(R.drawable.cube_closed_128)
               .into(view);
    }
});
```

## Installation

Add jitpack.io to your root build.gradle, eg:

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```
then add the dependency to your project build.gradle:

```groovy
dependencies {
    ...
    compile 'com.github.ypicoleal:FancyTab:0.0.2'
}
```

## Notes
This library is in alpha state currently, if you hava a question or find an error please open an issue i will keep working in this project

License
--------

    MIT License

    Copyright (c) 2017 Yamid Pico

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
