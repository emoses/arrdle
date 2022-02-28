(ns arrdle.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [arrdle.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
