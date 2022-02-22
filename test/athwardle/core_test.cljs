(ns athwardle.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [athwardle.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
