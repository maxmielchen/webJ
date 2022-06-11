package com.velocitypackage.materials.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class View implements Component
{
    private final List<Component> components;
    private final List<Button> buttons;
    
    public View()
    {
        components = Collections.synchronizedList(new ArrayList<>());
        buttons = Collections.synchronizedList(new ArrayList<>());
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
    public HyperTextElement getHTML()
    {
        StringBuilder content = new StringBuilder();
        for (Component component : components)
        {
            content.append(component.getHTML().compile());
        }
        HyperTextElement container = new HyperTextElement(HyperTextElement.TAG.DIV, new String(content));
        container.addClass("container");
        return container;
    }
}