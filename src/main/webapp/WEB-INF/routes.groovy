
get "/", forward: "/WEB-INF/pages/index.gtpl"
post "/store", forward: "/store.groovy"

get "/favicon.ico", redirect: "/images/gaelyk-small-favicon.png"
