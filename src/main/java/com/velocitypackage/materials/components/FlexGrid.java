package com.velocitypackage.materials.components;

import java.util.List;
import java.util.*;

public class FlexGrid implements Component
{
    private final List<Component> components;
    private final List<Button> buttons;
    
    private final Map<HyperTextElement.STYLE, String> styles;
    
    public FlexGrid()
    {
        components = new ArrayList<>();
        buttons = new ArrayList<>();
        styles = new HashMap<>();
    }
    
    @Override
    public void add(Component component)
    {
        components.add(component);
        if (component instanceof Button)
        {
            buttons.add((Button) component);
        }
    }
    
    @Override
    public void putStyle(HyperTextElement.STYLE option, String value)
    {
        styles.put(option, value);
    }
    
    @Override
    public void onClick(String id)
    {
        for (Component component : components)
        {
            component.onClick(id);
        }
        for (Button button : buttons)
        {
            button.onClick();
        }
    }
    
    @Override
    public HyperTextElement getContent()
    {
        StringBuilder content = new StringBuilder();
        for (Component component : components)
        {
            HyperTextElement element = new HyperTextElement(HyperTextElement.TAG.DIV, component.getContent().compile());
            element.addClass("col");
            element.addStyle(HyperTextElement.STYLE.MARGIN, "10px");
            content.append(element);
        }
        HyperTextElement row = new HyperTextElement(HyperTextElement.TAG.DIV, new String(content));
        row.addClass("row");
        HyperTextElement container = new HyperTextElement(HyperTextElement.TAG.DIV, row.compile());
        container.addClass("container");
        for (Map.Entry<HyperTextElement.STYLE, String> style : styles.entrySet())
        {
            container.addStyle(style.getKey(), style.getValue());
        }
        return container;
    }
}
