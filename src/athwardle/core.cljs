(ns athwardle.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [athwardle.events :as events]
   [athwardle.views :as views]
   [athwardle.config :as config]
   [goog.events :as gevent]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (gevent/unlisten js/window (.-KEYPRESS ^js gevent/EventType) events/key-handler)
  (gevent/unlisten js/window (.-KEYDOWN ^js gevent/EventType) events/key-handler)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (gevent/listen js/window (.-KEYDOWN ^js gevent/EventType) events/key-handler)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
