package io.qameta.allure.teamcity.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * eroshenkoam.
 * 11.04.17
 */
public final class ZipUtils {

    private ZipUtils() {
    }

    public static List<ZipEntry> listEntries(ZipFile zip, String path) {
        final Enumeration<? extends ZipEntry> entries = zip.entries();
        final List<ZipEntry> files = new ArrayList<>();
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            if (entry.getName().startsWith(path)) {
                files.add(entry);
            }
        }
        return files;
    }

    public static void zip(Path archive, Path base, List<Path> files) throws IOException {
        try (ArchiveOutputStream output = new ZipArchiveOutputStream(new FileOutputStream(archive.toFile()))) {
            for (Path file : files) {
                String entryName = base.toAbsolutePath().relativize(file).toString();
                ArchiveEntry entry = output.createArchiveEntry(file.toFile(), entryName);
                output.putArchiveEntry(entry);
                try (InputStream i = Files.newInputStream(file)) {
                    IOUtils.copy(i, output);
                }
                output.closeArchiveEntry();
            }
            output.finish();
        }
    }

}
