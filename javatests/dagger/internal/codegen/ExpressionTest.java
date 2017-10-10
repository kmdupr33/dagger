/*
 * Copyright (C) 2017 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dagger.internal.codegen;

import static com.google.common.truth.Truth.assertThat;

import com.google.testing.compile.CompilationRule;
import com.squareup.javapoet.CodeBlock;
import javax.lang.model.type.TypeMirror;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ExpressionTest {
  @Rule public CompilationRule compilationRule = new CompilationRule();

  interface Supertype {}

  interface Subtype extends Supertype {}

  @Test
  public void castTo() {
    TypeMirror subtype = type(Subtype.class);
    TypeMirror supertype = type(Supertype.class);
    Expression expression = Expression.create(subtype, CodeBlock.of("new $T() {}", subtype));

    Expression castTo = expression.castTo(supertype);

    assertThat(castTo.type()).isSameAs(supertype);
    assertThat(castTo.codeBlock().toString())
        .isEqualTo(
            "(dagger.internal.codegen.ExpressionTest.Supertype) "
                + "new dagger.internal.codegen.ExpressionTest.Subtype() {}");
  }

  private TypeMirror type(Class<?> clazz) {
    return compilationRule.getElements().getTypeElement(clazz.getCanonicalName()).asType();
  }
}
