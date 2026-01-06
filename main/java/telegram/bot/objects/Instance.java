package telegram.bot.objects;

import java.util.ArrayList;
import java.util.List;

public abstract class Instance {
    public String Name;
    private Instance Parent;
    private List<Instance> children;

    public Instance() {
        this.Name = this.getClass().getSimpleName();
        this.children = new ArrayList<>();
    }

    public Instance Parent() {
        return this.Parent;
    }

    public void SetParent(Instance parent) {
        if (this == parent) {return;}

        if (this.Parent != null) {
            this.Parent.children.remove(this);
        }

        this.Parent = parent;

        if (parent != null) {
            parent.children.add(this);
        }
    }

    public Instance GetChild(String name) {
        for (Instance child : this.children) {
            if (child.Name == name) {return child;}
        }
        return null;
    }

    public Instance[] Children() {
        Instance[] clone = new Instance[this.children.size()];
        for (int i = 0; i < this.children.size(); i++) {
            clone[i] = this.children.get(i);
        }
        return clone;
    }
}
