/*
 * Copyright 2026 Clyde Gerber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.javai18n.swing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static dev.javai18n.swing.SwingLogger.SWING_LOGGER;
import javax.imageio.ImageIO;

/**
 * A helper class that provides a static method to load a BufferedImage from a specified location.
 *
 * <p><b>Security note:</b> The {@link #getBufferedImageResource(String)} method will attempt to
 * read from the local filesystem as a last resort.  Callers are responsible for ensuring that
 * image path strings in resource bundles originate from trusted sources.</p>
 */
public final class BufferedImageResourceLoader
{
    private BufferedImageResourceLoader() {}
    /**
     * Cache of resource package names to their corresponding Modules, to avoid repeated StackWalker lookups.
     */
    private static final Map<String, Module> MODULE_CACHE = new ConcurrentHashMap<>();

    /**
     * Construct a BufferedImage from the location specified by str, first trying it as a local resource,
     * if that fails as a URL, and if that fails trying it as a file name.
     * @param str The location (URL or file) of the image.
     * @return The BufferedImage found at the location specified by str, or {@code null} if the
     *         image cannot be loaded from any of the three strategies.
     */
    public static BufferedImage getBufferedImageResource(String str)
    {
        try
        {
            String resourcePkg = "";
            if (str.lastIndexOf('/') >= 0)
            {
                resourcePkg = str.startsWith("/") ?
                    str.substring(1, str.lastIndexOf('/')) :
                    str.substring(0, str.lastIndexOf('/'));
            }
            resourcePkg = resourcePkg.replace("/", ".");
            Module module = BufferedImageResourceLoader.class.getModule();
            if (module.isNamed() && (resourcePkg.length() > 0))
            {
                module = MODULE_CACHE.computeIfAbsent(resourcePkg, pkg ->
                {
                    // Match the package of the resource to a package in the
                    // call stack to find the appropriate Module to load the resource.
                    // If there's not a match, use the Module for this Class.
                    Module defaultModule = BufferedImageResourceLoader.class.getModule();
                    StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
                    return walker.walk(s ->
                        s.filter(f -> f.getDeclaringClass().getPackage().getName().equals(pkg))
                         .map(f -> f.getDeclaringClass().getModule())
                         .findFirst()
                         .orElse(defaultModule));
                });
            }
            try (InputStream is = module.getResourceAsStream(str))
            {
                if (null != is)
                {
                    BufferedImage image = ImageIO.read(is);
                    if (null != image)
                    {
                        return image;
                    }
                }
            }
        }
        catch(IOException ex)
        {
            SWING_LOGGER.log(System.Logger.Level.DEBUG, "failed.to.load.image.as.resource", str, ex);
        }
        try
        {
            URI uri = new URI(str);
            BufferedImage image = ImageIO.read(uri.toURL());
            if (null != image)
            {
                return image;
            }
        }
        catch (IOException | IllegalArgumentException | URISyntaxException ex)
        {
            SWING_LOGGER.log(System.Logger.Level.DEBUG, "failed.to.load.image.as.url", str, ex);
        }
        try
        {
            BufferedImage image = ImageIO.read(new File(str));
            if (null != image)
            {
                return image;
            }
        }
        catch (IOException ex)
        {
            SWING_LOGGER.log(System.Logger.Level.DEBUG, "failed.to.load.image.as.file", str, ex);
        }
        SWING_LOGGER.log(System.Logger.Level.WARNING, "failed.to.load.image", str);
        return null;
    }
}
