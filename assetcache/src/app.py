import falcon
from falcon import Request, Response

from src.data_handlers import DataCollectors
from src.storages.primitive_json_db import PrimitiveJsonDB
from src.routes import AssetNamesRoute, LatestAssetRoute, Metrics, HealthCheckRoute


class AssetCacheAPI:

    def __init__(self, db: PrimitiveJsonDB, collectors: DataCollectors) -> None:
        self._app: falcon.App = falcon.App(cors_enable=True)
        self.collectors: DataCollectors = collectors

        self.asset_names: AssetNamesRoute = AssetNamesRoute(db)
        self.latest_stock: LatestAssetRoute = LatestAssetRoute(
            self.collectors.stock_cache, 'STOCK'
        )
        self.latest_crypto: LatestAssetRoute = LatestAssetRoute(
            self.collectors.crypto_cache, 'CRYPTO'
        )
        self.metrics = Metrics()
        self.healthz = HealthCheckRoute(self.collectors.stock_cache, self.collectors.crypto_cache)
        self.add_routes()

    @property
    def app(self) -> falcon.App:
        return self._app

    def add_routes(self) -> None:
        self._app.add_route("/metrics", self.metrics)
        self._app.add_route("/healthz", self.healthz)
        self._app.add_route("/api/v1/assets", self.asset_names)

        self._app.add_route("/api/v1/stock", self.asset_names, suffix="stock")
        self._app.add_route("/api/v1/stock/{symbol}", self.latest_stock)
        self._app.add_route("/api/v1/stock/{symbol}/trades", self.latest_stock,
                            suffix="trades")

        self._app.add_route("/api/v1/crypto", self.asset_names,
                            suffix="crypto")
        self._app.add_route("/api/v1/crypto/{symbol}", self.latest_crypto)
        self._app.add_route("/api/v1/crypto/{symbol}/trades",
                            self.latest_crypto, suffix="trades")
