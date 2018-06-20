package cn.ollyice.library.butterknife.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.List;

import static cn.ollyice.library.butterknife.compiler.BindingSet.UTILS;
import static cn.ollyice.library.butterknife.compiler.BindingSet.requiresCast;

final class FieldCollectionViewBinding {
  enum Kind {
    ARRAY("arrayOf"),
    LIST("listOf");

    final String factoryName;

    Kind(String factoryName) {
      this.factoryName = factoryName;
    }
  }

  final String name;
  private final TypeName type;
  private final Kind kind;
  private final boolean required;
  private final List<Id> ids;

  FieldCollectionViewBinding(String name, TypeName type, Kind kind, List<Id> ids,
      boolean required) {
    this.name = name;
    this.type = type;
    this.kind = kind;
    this.ids = ids;
    this.required = required;
  }

  CodeBlock render(boolean debuggable) {
    CodeBlock.Builder builder = CodeBlock.builder()
        .add("target.$L = $T.$L(", name, UTILS, kind.factoryName);
    for (int i = 0; i < ids.size(); i++) {
      if (i > 0) {
        builder.add(", ");
      }
      builder.add("\n");

      Id id = ids.get(i);
      builder.add("$T.find", UTILS);
      builder.add("RequiredView");
      builder.add("(source, (int)Utils.id(source.getContext(), \"$L\"), \"field '$L'\"", id.code, name);
      builder.add(")");
    }
    return builder.add(")").build();
  }
}
