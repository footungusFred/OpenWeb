from fastapi import FastAPI

app = FastAPI(title="OpenWeb Search", version="0.1.0")


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok", "service": "openweb-search"}


def run() -> None:
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8001)
