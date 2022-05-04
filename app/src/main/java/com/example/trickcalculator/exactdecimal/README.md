# Exact Decimal

## Overview

An **ExactDecimal** is a representation of a rational or irrational number, to the maximum precision allowed by Kotlin (and therefore by Java).
Unlike a **BigDecimal**, it doesn't specify a level of precision.
Instead, it stores individual expressions (which may contain irrational numbers) as strings, and simplifies them before computing a specific numeric value.

Currently, the **ExactDecimal** class supports numbers and **pi**, but additional values, such as **e**, roots, and trigonometry, are planned.

## Important Definitions

All definitions are currently given in terms of constants and pi, but will be modified when additional functionality is added

* **Term**: c * pi^exp, for some constant **c** and exponent **exp**
* **Expression**: sum of terms c_k * pi^exp_k, where **exp** values may not be distinct
* **Simplified expression**: expression where each term has a distinct **exp** value
* **Term list**: representation of expression where each c * pi^exp value is stored as an individual term, which represents a summation over the given terms
* **Expression list**: list of expressions, which represents a multiplication over the given expressions

## Classes
* **ExactDecimal**: number implementation to store exact representation of a number, using expression lists for the numerator and denominator
* **Term**: a single term, containing a coefficient and an exponent of pi, which can be any whole number
* **Expression**: represents the expression as a term list
* **TermList**: list of terms, an alias for **List<Term>**
* **ExprList**: list of expressions, an alias for **List<Expression<**
