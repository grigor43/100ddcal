get "/", forward: "/WEB-INF/pages/index.gtpl"
get "/e/index", forward: "/WEB-INF/pages/embed.gtpl"
get "/api/data", forward: "/data.groovy"
post "/store", forward: "/store.groovy"

get "/favicon.ico", redirect: "/images/gaelyk-small-favicon.png"
