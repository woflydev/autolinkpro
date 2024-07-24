# AutoLink Pro

A slightly cancerous, FlatLaf-based Swing application for car rental management. 

## Building

This project uses both `Maven` and local libraries. Ensure you have `Maven` installed, either externally or through an IDE such as `IntelliJ IDEA`. 

Open project, reload `Maven`, Generate Sources & Update Folders, then execute:

```bash
mvn clean compile assembly:single
```

This will create a executable `.jar` file in the `target/` directory.

## Using

`AutoLinkPro` will automatically detect a first boot if no existing data is found. In the case no data is found, it will automatically create a `data/` folder in the directory where it was run.

The default login (with superuser permissions) is `admin@autolinkpro.com` with password `admin`.

## Restoring Corrupt Instances

`AutoLinkPro` will attempt to recover any usable data if it can detect an existing `data` directory. However, note that you will not be able to log in unless an `owner.json` file is defined in the `data/users/` directory.