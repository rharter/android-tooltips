# Android ToolTips [Play Store Demo](https://play.google.com/store/apps/details?id=com.ryanharter.android.tooltips.sample)

ToolTips is an Open Source Android library that allows developers to easily display tooltips for views.

## Usage

1. Add a `com.ryanharter.android.tooltips.ToolTipLayout` view to your layout such that it fills the view view and overlays everything else.
1. Create `ToolTip` instances using the `Builder` class.
1. Add the `ToolTip`s to your view.

## Example

### layout.xml

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.ryanharter.android.tooltips.sample.MyActivity">

    <!-- Layout elements go here -->

    <!-- Add the ToolTipLayout to your view, ensuring it's at the end -->
    <!-- of the FrameLayout (or RelativeLayout) so it overlays other  -->
    <!-- views.                                                       -->
    <com.ryanharter.android.tooltips.ToolTipLayout
        android:id="@+id/tooltip_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>
```

### Activity.java (or Fragment.java)

```java
// Get the tooltip layout
ToolTipLayout tipContainer = (ToolTipLayout) findViewById(R.id.tooltip_container);

// Create a content view however you'd like
// ...

// Create a ToolTip using the Builder class
ToolTip t = new Builder(myContext)
        .anchor(myAnchorView)      // The view to which the ToolTip should be anchored
        .gravity(Gravity.TOP)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
        .color(Color.RED)          // The color of the pointer arrow
        .pointerSize(POINTER_SIZE) // The size of the pointer
        .contentView(contentView)  // The actual contents of the ToolTip
        .build();

// Add the ToolTip to the view, using the default animations
tipContainer.addTooltip(t);
```

## Developed By

* Ryan Harter

## License

```
Copyright 2013 Ryan Harter

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```