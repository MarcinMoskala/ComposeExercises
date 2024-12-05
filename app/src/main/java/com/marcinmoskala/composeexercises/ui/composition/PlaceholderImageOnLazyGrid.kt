package com.marcinmoskala.composeexercises.ui.composition

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.marcinmoskala.composeexercises.R
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun PlaceholderImageOnLazyGrid() {
    var loaded by remember { mutableIntStateOf(0) }
    Column {
        Text("Loaded: $loaded", fontSize = 40.sp)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 200.dp),
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        ) {
            items(images) { imageUrl ->
                AsyncImage(
//                    placeholder = painterResource(R.drawable.placeholder),
                    model = imageUrl,
                    onLoading = { loaded++ },
                    contentDescription = "Photo from url $imageUrl",
                )
            }
        }
    }
}

val images = persistentListOf(
    "https://marcinmoskala.com/kt-academy-articles/promotion/blockhound.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/viewmodel-stateflow-sharedflow-channel.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/dispatcher-for-backend.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/droidcontest_en.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book-workshop-promotion-2025.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/101_why.jpg",
    "https://marcinmoskala.com/kt-academy-articles/images/workshop-choose.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/respect_contracts.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/203_job.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/finance-by-company.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/contracts_documentation.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/207_dispatchers.jpg",
    "https://marcinmoskala.com/kt-academy-articles/images/sharedflow_vs_stateflow_cover.png",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/api_stability.jpg",
    "https://marcinmoskala.com/kt-academy-articles/images/power_assert_cover.png",
    "https://marcinmoskala.com/kt-academy-articles/images/union-types-intro-cover.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/coroutines-advantages.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/204_cancellation.jpg",
    "https://marcinmoskala.com/kt-academy-articles/images/var_vs_mutable_cover.png",
    "https://marcinmoskala.com/kt-academy-articles/promotion/learn-with-mm.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book-release-third-coroutine.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/books-update-2024-03.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/network-client-threads.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/tweet_3.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/element_visibility.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/wrapping_api.jpg",
    "https://marcinmoskala.com/kt-academy-articles/renatocosta/promotion/pattern_for_composing_flows.jpg",
    "https://marcinmoskala.com/kt-academy-articles/renatocosta/promotion/nonblocking_spring_mvc.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book-workshop-promotion-2024.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/generics.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/flat_map_merge.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/promo-kotlinconf.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/202_coroutine_context.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/abstraction_code_changes.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/single_layer_of_abstraction.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/abstraction_design.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/utils.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/13_static_analysis.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book-functional-now-with-exercises.png",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/knowledge.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/risc-dice-dilemma.png",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book-exercises-challenge.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/07_JS.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/12_compiler_plugin.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/10_annotation_processing.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/collection_types.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/11_symbol_processing.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/caching.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book-essentials-now-with-exercises.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/data_classes.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/books-for-photos.png",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/object_declarations.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/09_reflection_3.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/09_reflection_2.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/kotlin-books-for-universities-and-conferences.png",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/09_reflection_1.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/books-kotlin-for-developers-ready.png",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/extensions.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/dependency_injection.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/books-ready-for-kotlin-2_0.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/06_multiplatform_3.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/06_multiplatform_2.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/06_multiplatform_1.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/lighthouse.png",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book-release-second-effective.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/sealed.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/synchronization.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/05_java_interop_4.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/05_java_interop_3.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/05_java_interop_2.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/05_java_interop_1.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book_release_advanced_kotlin.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/objects.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book-workshop-promotion-2023.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/04_contracts.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/eliminating-coroutine-races.png",
    "https://marcinmoskala.com/kt-academy-articles/promotion/surname-survival-simulation.png",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/type_system.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/305_understanding_flow.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book-release-second-coroutine.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/why_you_should_consider_migrating_your_gradle_scripts_to_kotlin_dsl.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/03_property_delegation_4_map.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/03_property_delegation_3_observable.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/405_best_practices.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/kotlin_revolutionary_announcement.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/cc_recipes.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/03_property_delegation_2_lazy.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/03_property_delegation_1.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/nullability.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/02_interface_delegation.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/tweet_2.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/basic_values.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/01_generics_variance_3.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/01_generics_variance_2.jpg",
    "https://marcinmoskala.com/advanced-kotlin-book/promotion/01_generics_variance_1.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/enum.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/operators.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/for.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/tweet_1.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/first_program.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/dispatcher-loom.png",
    "https://marcinmoskala.com/functional_kotlin_book/promotion/13_arrow_3.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book_release_kotlin_essentials.jpg",
    "https://marcinmoskala.com/kotlin_essentials_book/promotion/functions.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/slowing-down-coroutines.png",
    "https://marcinmoskala.com/coroutines_book/promotion/401_article_use_cases.jpg",
    "https://marcinmoskala.com/functional_kotlin_book/promotion/13_arrow_2.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/401_3_use_cases.jpg",
    "https://marcinmoskala.com/functional_kotlin_book/promotion/13_arrow_1.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/401_2_use_cases.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/401_1_use_cases.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book_release_functional_kotlin.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/readability.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/nullable_result.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/expectations.jpg",
    "https://marcinmoskala.com/functional_kotlin_book/promotion/contex_receivers.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/mutability.jpg",
    "https://marcinmoskala.com/functional_kotlin_book/promotion/scope_functions.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/type-modelling-kotlin.png",
    "https://marcinmoskala.com/functional_kotlin_book/promotion/DSLs.jpg",
    "https://marcinmoskala.com/functional_kotlin_book/promotion/lambda_expressions.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/oop-vs-fp.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/essential-programming-nomenclature.png",
    "https://marcinmoskala.com/functional_kotlin_book/promotion/function_references.jpg",
    "https://marcinmoskala.com/software-testing-articles/best_principles.png",
    "https://marcinmoskala.com/software-testing-articles/leveraging_AI.png",
    "https://marcinmoskala.com/software-testing-articles/software_testing.png",
    "https://marcinmoskala.com/funny_programming/how_to_write.jpg",
    "https://marcinmoskala.com/persistent-memory/design.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/why-is-kotlin-your-next-programming-language.png",
    "https://marcinmoskala.com/persistent-memory/dictionary.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/code-or-no-code.png",
    "https://marcinmoskala.com/persistent-memory/introducing.jpg",
    "https://marcinmoskala.com/persistent-memory/in-memory.jpg",
    "https://marcinmoskala.com/persistent-memory/introduction.png",
    "https://marcinmoskala.com/funny_programming/comics.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/book-release-coroutines.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/309_state_flow.jpg",
    "https://marcinmoskala.com/funny_programming/video.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/adventanswers.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/adventweek4.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/adventweek3.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/adventweek2.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/adventweek1.jpg",
    "https://marcinmoskala.com/kt-academy-articles/promotion/references.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/210_testing.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/208_constructing_scope.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/groupBy.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/associate.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/sequence.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/eliminate_object_references.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/inline_classes.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/104_under_the_hood.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/inline.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/unnecessary_object.jpg",
    "https://kt.academy/images/article_traits_for_testing.jpg",
    "https://marcinmoskala.com/coroutines_book/promotion/103_suspension.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/factory_functions.jpg",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/member_extensions.jpg",
    "https://kt.academy/logo.png",
    "https://marcinmoskala.com/EffectiveKotlin-Book/promotion/composition.jpg"
)