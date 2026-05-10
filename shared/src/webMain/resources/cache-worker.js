const CACHE_NAME = 'clock-of-clocks-v1';
const ASSETS_TO_CACHE = [
    '/',
    'index.html',
    'styles.css',
    'shared.js',
    'images/clock-16.png',
    'images/clock-32.png',
    'images/clock-96.png',
    'images/clock-120.png'
];

self.addEventListener('install', event => {
    event.waitUntil(
        caches
            .open(CACHE_NAME)
            .then(cache => cache.addAll(ASSETS_TO_CACHE))
            .catch((error) => {
                console.error("Cache failed:", error);
            })
    );
    self.skipWaiting();
});

self.addEventListener('activate', event => {
    event.waitUntil(
        caches.keys().then(keys => {
            return Promise.all(
                keys.filter(key => key !== CACHE_NAME)
                    .map(key => caches.delete(key))
            );
        })
        .then(() => self.clients.claim())
    );
});

// 3. Sự kiện Fetch: "Đánh chặn" các yêu cầu mạng
self.addEventListener('fetch', event => {
    event.respondWith(
        caches.match(event.request).then(cachedResponse => {
            // Nếu tìm thấy trong cache, trả về luôn
            if (cachedResponse) {
                return cachedResponse;
            }
            // Nếu không thấy, tải từ mạng
            return fetch(event.request).then(networkResponse => {
                // (Tùy chọn) Lưu thêm vào cache các tài nguyên phát sinh khi đang lướt web
                if (event.request.method === 'GET' && networkResponse.status === 200) {
                    const cacheCopy = networkResponse.clone();
                    caches.open(CACHE_NAME).then(cache => {
                        cache.put(event.request, cacheCopy);
                    });
                }
                return networkResponse;
            });
        })
    );
});
