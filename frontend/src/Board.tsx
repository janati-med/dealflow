import { useCallback, useEffect, useState } from 'react'
import { ApiError, api } from './api'
import { STAGES } from './types'
import type { Customer, Deal, Stage } from './types'

export default function Board({ onUnauthorized }: { onUnauthorized: () => void }) {
    const [deals, setDeals] = useState<Deal[]>([])
    const [customers, setCustomers] = useState<Customer[]>([])
    const [error, setError] = useState('')
    const [title, setTitle] = useState('')
    const [value, setValue] = useState('')
    const [customerId, setCustomerId] = useState('')

    const handleError = useCallback(
        (e: unknown) => {
            if (e instanceof ApiError && e.status === 401) onUnauthorized()
            else if (e instanceof ApiError && e.status === 409) setError('This deal is already closed')
            else setError('Something went wrong')
        },
        [onUnauthorized],
    )

    const load = useCallback(async () => {
        try {
            const [d, c] = await Promise.all([api.deals(), api.customers()])
            setDeals(d)
            setCustomers(c)
            setError('')
        } catch (e) {
            handleError(e)
        }
    }, [handleError])

    useEffect(() => {
        load()
    }, [load])

    const customerName = new Map(customers.map((c) => [c.id, c.name]))

    async function move(id: number, stage: Stage) {
        try {
            await api.moveDeal(id, stage)
            await load()
        } catch (e) {
            handleError(e)
        }
    }

    async function addDeal(event: React.FormEvent) {
        event.preventDefault()
        try {
            await api.createDeal({ title, value: Number(value), customerId: Number(customerId) })
            setTitle('')
            setValue('')
            await load()
        } catch (e) {
            handleError(e)
        }
    }

    return (
        <div>
            <form className="new-deal" onSubmit={addDeal}>
                <input placeholder="Deal title" value={title} onChange={(e) => setTitle(e.target.value)} required />
                <input type="number" min="0" placeholder="Value (EUR)" value={value} onChange={(e) => setValue(e.target.value)} required />
                <select value={customerId} onChange={(e) => setCustomerId(e.target.value)} required>
                    <option value="">Customer...</option>
                    {customers.map((c) => (
                        <option key={c.id} value={c.id}>{c.name}</option>
                    ))}
                </select>
                <button type="submit">Add deal</button>
            </form>

            {error && <p className="error">{error}</p>}

            <div className="board">
                {STAGES.map((stage) => {
                    const stageDeals = deals.filter((d) => d.stage === stage)
                    const total = stageDeals.reduce((sum, d) => sum + d.value, 0)
                    return (
                        <section key={stage} className="column">
                            <h2>{stage} <small>({stageDeals.length})</small></h2>
                            <p className="total">{total.toLocaleString('de-DE')} EUR</p>
                            {stageDeals.map((deal) => (
                                <article key={deal.id} className="card">
                                    <strong>{deal.title}</strong>
                                    <span>{customerName.get(deal.customerId) ?? 'Unknown customer'}</span>
                                    <span>{deal.value.toLocaleString('de-DE')} EUR</span>
                                    {stage !== 'GEWONNEN' && stage !== 'VERLOREN' && (
                                        <div className="actions">
                                            {STAGES.filter((s) => s !== stage).map((s) => (
                                                <button key={s} onClick={() => move(deal.id, s)}>{s}</button>
                                            ))}
                                        </div>
                                    )}
                                </article>
                            ))}
                        </section>
                    )
                })}
            </div>
        </div>
    )
}