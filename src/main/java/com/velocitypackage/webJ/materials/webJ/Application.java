package com.velocitypackage.webJ.materials.webJ;

import com.velocitypackage.webJ.materials.hypertext.HyperTextPage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * IMPORTANT -> constructor parameter always overwrite with null
 * @author maxmielchen
 */
@SuppressWarnings("unused")
public abstract class Application implements Cloneable
{
    private final Set<HyperTextPage> pages = new HashSet<>();
    private String currentPath = "/";
    private String applicationName;
    private String faviconBase64;
    
    private Runnable onForceUpdate;
    
    /**
     * Adds a new Page
     * @param page the page
     */
    public final void addPage(HyperTextPage page)
    {
        this.pages.add(page);
    }
    
    /**
     * sets the application name
     * @param name the application name
     */
    public final void setApplicationName(String name)
    {
        this.applicationName = name;
    }
    
    /**
     * sets the application icon
     * @param favicon the application icon (only *.ico)
     * @throws IllegalArgumentException if the file is not a valid image or null
     */
    public void setFavicon(File favicon) throws IllegalArgumentException, IOException
    {
        if(favicon == null)
        {
            throw new IllegalArgumentException("favicon cannot be null");
        }
        this.faviconBase64 = toBase64(favicon);
    }
    
    /**
     * sets the application icon
     * @param favicon the application icon (only *.ico)
     * @throws IllegalArgumentException if the file is not a valid image or null
     */
    public void setFavicon(String favicon) throws IllegalArgumentException
    {
        if(favicon == null)
        {
            throw new IllegalArgumentException("favicon cannot be null");
        }
        this.faviconBase64 = favicon;
    }
    
    /**
     * Sets the force update
     * @param runnable force update methode
     */
    public void setForceUpdate(Runnable runnable)
    {
        this.onForceUpdate = runnable;
    }
    
    /**
     * Returns an interpretation of all components as html
     * @return string as html
     */
    public final String getTextRepresentation()
    {
        for (HyperTextPage hyperTextPage : pages) {
            if (hyperTextPage.getPath().equals(currentPath)) {
                return hyperTextPage.getTextRepresentation();
            }
        }
        return "<div class=\"text-center d-block vh-100 position-fixed vw-100\"><hr /><h3 class=\"text-center\">404</h3><p class=\"text-center\">This page doesn't exist...</p><hr /></div>";
    }
    
    /**
     * returns the application name
     * @return application name
     */
    public final String getApplicationName()
    {
        return this.applicationName;
    }
    
    /**
     * returns the application favicon
     * @return application favicon
     */
    public String getFavicon()
    {
        return faviconBase64;
    }
    
    /**
     * get all pages
     * @return array of pages
     */
    public HyperTextPage[] getPages()
    {
        HyperTextPage[] pages = new HyperTextPage[this.pages.size()];
        this.pages.toArray(pages);
        return pages;
    }
    
    /**
     * update the UI
     */
    public void forceUpdate()
    {
        if (onForceUpdate == null)
        {
            return;
        }
        onForceUpdate.run(); //force update
    }
    
    /**
     * onMessage
     * @param message the message as string
     */
    public final void onMessage(String message) throws NotSupportedMessageFormat
    {
        if (message.trim().startsWith("path:")) {
            currentPath = message.replaceAll("path:", "").trim();
            return;
        }
        //id:<id> inputs:{}
        //id:<id> inputs:{<inputID>??<value>;;...}
        try
        {
            String id = message.split(" ", 2)[0].trim().replaceAll("id:", "").trim();
            String[] inputs = message.split(" ", 2)[1].trim().replaceAll("inputs:\\{", "").replaceAll("}", "").split(";;");
            Map<String, String> inputMap = new HashMap<>();
            if (message.contains("??"))
            {
                for (String s : inputs)
                {
                    inputMap.put(s.split("\\?\\?")[0], s.split("\\?\\?")[1]);
                }
            }
            for (HyperTextPage hyperTextPage : pages)
            {
                if (hyperTextPage.getPath().equals(currentPath)) {
                    hyperTextPage.onMessage(id, inputMap);
                }
            }
        } catch (Exception ignore)
        {
            throw new NotSupportedMessageFormat("id:<id> inputs:{}", "id:<id> inputs:{<inputID>??<value>;;...}");
        }
    }
    
    /**
     * Convert a file into a image base64 interpretation
     * @param image file of image
     * @return the base64 version
     * @throws IOException throws if the image didn't exist
     */
    private static String toBase64(File image) throws IOException
    {
        FileInputStream stream = new FileInputStream(image);
        int bufLength = 2048;
        byte[] buffer = new byte[2048];
        byte[] data;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int readLength;
        while ((readLength = stream.read(buffer, 0, bufLength)) != - 1)
        {
            out.write(buffer, 0, readLength);
        }
        data = out.toByteArray();
        String imageString = Base64.getEncoder().withoutPadding().encodeToString(data);
        out.close();
        stream.close();
        return imageString;
    }
    
    @Override
    public final Application clone() throws CloneNotSupportedException
    {
        try
        {
            return this.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            throw new CloneNotSupportedException("Async Application Clients isn't working...");
        }
    }
}