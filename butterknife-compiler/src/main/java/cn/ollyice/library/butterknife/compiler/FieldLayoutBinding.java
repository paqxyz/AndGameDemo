package cn.ollyice.library.butterknife.compiler;

import com.squareup.javapoet.CodeBlock;

/**
 * Created by admin on 2018/6/6.
 */

public class FieldLayoutBinding implements ResourceBinding {
    private final Id id;
    private final String name;

    FieldLayoutBinding(Id id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override public Id id() {
        return id;
    }

    @Override public boolean requiresResources(int sdk) {
        return false;
    }

    @Override public CodeBlock render(int sdk) {
        return CodeBlock.of("target.$L = (int)Utils.id(context,\"$L\")", name, id.code);
    }
}
