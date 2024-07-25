# AutolinkFiler

AutolinkFiler is just another open-source `gson` wrapper for a very specific project.

It provides extensible, generic methods for creating, deleting, updating entities on any given harddrive through the JSON file format, along with a multitude of other utility methods.

`FileUtils.java` is the main class you will be implementing methods for. You can find an example of how these methods are implemented under `CarUtils.java`. But feel free to do whatever you want.

## Getting Started

To get started, you will have to install `gson`. This is very easy if you have `Maven`. If you're using `IntelliJ`, chances are that you already have `Maven` installed.

Even if you don't have `Maven`, installation is very easy if you have a modern IDE. Just search "Install Maven [IDE NAME HERE]".

Once it's all nice and installed, open your `pom.xml`, in your root directory.

Copy the following code snippet into the section that says `<dependencies></dependencies>`. You **might** not have any dependencies and you **may** have to create this yourself.

```pom
<dependency> <!-- for JSON serialization -->
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

So now, your `pom.xml` should look something like this:

```pom
<blah blah blah>

<dependencies>
    <dependency> <!-- for JSON serialization -->
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
</dependencies>

<blah blah blah>
```

That's all you need to do! `gson`, the library that is used to serialize JSON files, should now be installed.

If you're getting a **red line** underneath your `<dependency>`, don't panic! Go to the rightmost toolbar, click the `M` icon (if `IntelliJ`), and the `Maven` configuration should pop up.

Simply press `Reload All Maven Projects` and `Generate Sources and Update Folders for All Projects` and it should download the required libraries and auto-configure your project to work.

## Using AutolinkFiler

This library includes a fully extensible, customizable list-based save/load system. You can see that, for example, in `CarUtils.java`:

```java
public static List<Car> getCarList() { 
    return FileUtils.getEntityList(CARS_FILE, Car[].class); 
}
```
The above code snippet uses the `FileUtils` class to return a list of cars. It does this by first importing a static variable from `Config.java` (although you can define your own static `String` to whenever you want).

In this case, the `String` points to a directory named `data/system/cars.json`, which is precisely where `FileUtils` will search to find the data. If extracted successfully, it will return a list with objects of type `Car`. You can then use this output to do whatever you want!

Look into `CarUtils` for more examples, including updating a `Car`, deleting one, or making a new one.

Similar to this, you could make a new file called `UserUtils` and have that handle all of the user stuff, on top of `FileUtils`.

There's also a bunch of documentation and comments in `FileUtils.java` that should help you understand how to implement your own methods.

## Thanks for reading!

Made with love by `woflydev`. This project is under the GNU-GPLv3 License.